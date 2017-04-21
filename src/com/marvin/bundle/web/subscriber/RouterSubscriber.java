package com.marvin.bundle.web.subscriber;

import com.marvin.bundle.framework.handler.event.GetModelAndViewEvent;
import com.marvin.bundle.framework.handler.event.HandlerEvent;
import com.marvin.bundle.framework.handler.event.HandlerEvents;
import com.marvin.component.event.EventSubscriber;

import com.marvin.component.routing.Router;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouterSubscriber extends EventSubscriber<HandlerEvent<HttpServletRequest, HttpServletResponse>> {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    
    private final Router router;

    public RouterSubscriber(Router router) {
        this.router = router;
    }
    
    public void onRequest(HandlerEvent<HttpServletRequest, HttpServletResponse> event){
        
        if(event instanceof GetModelAndViewEvent) {
            GetModelAndViewEvent<HttpServletRequest, HttpServletResponse> e = (GetModelAndViewEvent) event;
            
            HttpServletRequest request = e.getRequest();
            
            Map<String, Object> attributes = this.router.match(request.getServletPath());
        
            if(attributes != null) {
                attributes.forEach(request::setAttribute);
            }
            
            e.setRequest(request);
        }
    }

    @Override
    public Map<String, Consumer<HandlerEvent<HttpServletRequest, HttpServletResponse>>> getSubscribedEvents() {
        Map<String, Consumer<HandlerEvent<HttpServletRequest, HttpServletResponse>>> subscribed = new HashMap<>();
        subscribed.put(HandlerEvents.REQUEST, this::onRequest);
        return subscribed;
    }
    
}
