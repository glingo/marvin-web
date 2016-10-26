package com.marvin.bundle.web;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.marvin.component.event.subscriber.SubscriberInterface;
import com.marvin.component.routing.Router;

import com.marvin.bundle.web.event.GetResponseEvent;
import com.marvin.bundle.web.event.RequestHandlerEvent;
import com.marvin.bundle.web.event.RequestHandlerEvents;

import javax.servlet.http.HttpServletRequest;

public class RouterSubscriber implements SubscriberInterface<RequestHandlerEvent> {

    private final Router router;

    public RouterSubscriber(Router router) {
        this.router = router;
    }
    
    public void onRequest(RequestHandlerEvent event){
        if(event instanceof GetResponseEvent) {
            HttpServletRequest request = event.getRequest();
            
            HashMap<String, Object> attributes = router.match(request.getRequestURI());
        
            if(attributes != null) {
                attributes.forEach(request::setAttribute);
            }
            
            event.setRequest(request);
        }
    }

    @Override
    public Map<String, Consumer<RequestHandlerEvent>> getSubscribedEvents() {
        Map<String, Consumer<RequestHandlerEvent>> subscribed = new HashMap<>();
        subscribed.put(RequestHandlerEvents.REQUEST, this::onRequest);
        return subscribed;
    }
    
}
