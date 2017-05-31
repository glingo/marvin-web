package com.marvin.bundle.web.subscriber;

import com.marvin.component.mvc.ModelAndView;
import event_old.EventConsumer;
import com.marvin.component.mvc.view.View;
import event_old.EventSubscriber;
import com.marvin.bundle.framework.mvc.Handler;
import com.marvin.bundle.framework.mvc.event.GetResultEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvents;
import com.marvin.component.mvc.view.ViewInterface;
import com.marvin.component.util.ClassUtils;

import com.marvin.component.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceSubscriber extends EventSubscriber<HandlerEvent> {

    /**
     * Default Servlet name used by Tomcat, Jetty, JBoss, and GlassFish
     */
    private static final String COMMON_DEFAULT_SERVLET_NAME = "default";

    /**
     * Default Servlet name used by Google App Engine
     */
    private static final String GAE_DEFAULT_SERVLET_NAME = "_ah_default";

    /**
     * Default Servlet name used by Resin
     */
    private static final String RESIN_DEFAULT_SERVLET_NAME = "resin-file";

    /**
     * Default Servlet name used by WebLogic
     */
    private static final String WEBLOGIC_DEFAULT_SERVLET_NAME = "FileServlet";

    /**
     * Default Servlet name used by WebSphere
     */
    private static final String WEBSPHERE_DEFAULT_SERVLET_NAME = "SimpleFileServlet";
    
    private static final String DEFAULT_RESOURCE_PATH = "/resources/";
    
    private String defaultServletName;
    private String resourcePath;
    
    public ResourceSubscriber() {
        this.resourcePath = DEFAULT_RESOURCE_PATH;
    }

    public ResourceSubscriber(String defaultServletName) {
        this.defaultServletName = defaultServletName;
    }

    public void onRequest(HandlerEvent event) {
        if (!ClassUtils.isAssignableValue(GetResultEvent.class, event)) {
            return;
        }
        
        GetResultEvent e = (GetResultEvent) event;

        if (!ClassUtils.isAssignableValue(HttpServletRequest.class, e.getRequest())) {
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) e.getRequest();
            
        if(!request.getServletPath().startsWith(this.resourcePath)) {
            return;
        }

        ServletContext servletContext = request.getServletContext();

        if(this.defaultServletName == null || !StringUtils.hasLength(this.defaultServletName)) {

            if (servletContext.getNamedDispatcher(COMMON_DEFAULT_SERVLET_NAME) != null) {
                this.defaultServletName = COMMON_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(GAE_DEFAULT_SERVLET_NAME) != null) {
                this.defaultServletName = GAE_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(RESIN_DEFAULT_SERVLET_NAME) != null) {
                this.defaultServletName = RESIN_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(WEBLOGIC_DEFAULT_SERVLET_NAME) != null) {
                this.defaultServletName = WEBLOGIC_DEFAULT_SERVLET_NAME;
            } else if (servletContext.getNamedDispatcher(WEBSPHERE_DEFAULT_SERVLET_NAME) != null) {
                this.defaultServletName = WEBSPHERE_DEFAULT_SERVLET_NAME;
            } else {
                throw new IllegalStateException("Unable to locate the default servlet for serving static content. Please set the 'defaultServletName' property explicitly.");
            }
        }
            
        RequestDispatcher dispatcher = servletContext.getNamedDispatcher(this.defaultServletName);
        ViewInterface view = new ResourceView(request.getServletPath(), dispatcher);
        e.setResult(new ModelAndView(view));
    }

    @Override
    public Map<String, EventConsumer<HandlerEvent>> getSubscribedEvents() {
        Map<String, EventConsumer<HandlerEvent>> subscribed = new HashMap<>();
        subscribed.put(HandlerEvents.REQUEST, this::onRequest);
        return subscribed;
    }
    
    private class ResourceView extends View<HttpServletRequest, HttpServletResponse> {
        
        private final RequestDispatcher dispatcher;

        public ResourceView(String name, RequestDispatcher dispatcher) {
            super(name);
            this.dispatcher = dispatcher;
        }
        
        @Override
        public void render(
                Handler<HttpServletRequest, HttpServletResponse> handler, 
                HashMap<String, Object> model, 
                HttpServletRequest request, 
                HttpServletResponse response) throws Exception {
            this.dispatcher.forward(request, response);
        }

        @Override
        public void load() throws Exception {
        }
    }
}
