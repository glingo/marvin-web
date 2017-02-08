package com.marvin.bundle.web.subscriber;

import com.marvin.bundle.framework.handler.event.GetModelAndViewEvent;
import com.marvin.bundle.framework.handler.event.HandlerEvent;
import com.marvin.bundle.framework.handler.event.HandlerEvents;
import com.marvin.bundle.framework.mvc.ModelAndView;
import com.marvin.bundle.framework.mvc.view.IView;
import com.marvin.bundle.framework.mvc.view.View;
import com.marvin.component.event.EventSubscriber;

import com.marvin.component.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceSubscriber extends EventSubscriber<HandlerEvent<HttpServletRequest, HttpServletResponse>> {

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

    public void onRequest(HandlerEvent<HttpServletRequest, HttpServletResponse> event) {

        if (event instanceof GetModelAndViewEvent) {
            GetModelAndViewEvent<HttpServletRequest, HttpServletResponse> e = (GetModelAndViewEvent) event;

            HttpServletRequest request = e.getRequest();
            
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
            IView view = new ResourceView(dispatcher);
            e.setModelAndView(new ModelAndView(view));
        }
    }

    @Override
    public Map<String, Consumer<HandlerEvent<HttpServletRequest, HttpServletResponse>>> getSubscribedEvents() {
        Map<String, Consumer<HandlerEvent<HttpServletRequest, HttpServletResponse>>> subscribed = new HashMap<>();
        subscribed.put(HandlerEvents.REQUEST, this::onRequest);
        return subscribed;
    }
    
    private class ResourceView extends View<HttpServletRequest, HttpServletResponse> {
        
        private final RequestDispatcher dispatcher;

        public ResourceView(RequestDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }
        
        @Override
        public void render(HashMap<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            this.dispatcher.forward(request, response);
        }
    }
}
