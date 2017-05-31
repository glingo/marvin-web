package com.marvin.bundle.web.subscriber;

import event_old.EventConsumer;
import event_old.EventSubscriber;
import com.marvin.bundle.framework.mvc.event.FilterRequestEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvents;

import com.marvin.component.routing.Router;
import com.marvin.component.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class RouterSubscriber extends EventSubscriber<HandlerEvent> {

    protected final Logger logger = Logger.getLogger(getClass().getName());
    
    private final Router router;

    public RouterSubscriber(Router router) {
        this.router = router;
    }
    
    public void onRequest(HandlerEvent event){
        if(!ClassUtils.isAssignableValue(FilterRequestEvent.class, event)) {
            return;
        }
        
        FilterRequestEvent e = (FilterRequestEvent) event;
        if (!ClassUtils.isAssignableValue(HttpServletRequest.class, e.getRequest())) {
            return;
        }
        
        HttpServletRequest request = (HttpServletRequest) e.getRequest();
        Map<String, Object> attributes = this.router.match(request.getServletPath());

        if(attributes != null) {
            attributes.forEach(request::setAttribute);
        }

//                e.setRequest(request);
        }

    @Override
    public Map<String, EventConsumer<HandlerEvent>> getSubscribedEvents() {
        Map<String, EventConsumer<HandlerEvent>> subscribed = new HashMap<>();
        subscribed.put(HandlerEvents.REQUEST, this::onRequest);
        return subscribed;
    }
    
}
