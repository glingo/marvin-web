package com.marvin.bundle.web.subscriber;

import com.marvin.bundle.framework.mvc.event.FilterRequestEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvent;
import com.marvin.bundle.framework.mvc.event.HandlerEvents;
import com.marvin.component.event.dispatcher.DispatcherInterface;
import com.marvin.component.event.handler.Handler;
import com.marvin.component.event.subscriber.Subscriber;

import com.marvin.component.routing.Router;
import com.marvin.component.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RouterSubscriber extends Subscriber {
    
    private final Router router;

    public RouterSubscriber(Router router) {
        this.router = router;
    }
    
    public Handler<FilterRequestEvent> onRequest(){
        return event -> {
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
        };
    }

    @Override
    public void subscribe(DispatcherInterface dispatcher) {
        dispatcher.register(FilterRequestEvent.class, onRequest());
    }
}
