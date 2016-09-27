package com.marvin.bundle.web.routing.event;

import com.marvin.component.event.subscriber.SubscriberInterface;
import com.marvin.component.routing.Router;
import com.marvin.bundle.web.event.GetResponseEvent;
import com.marvin.bundle.web.event.RequestHandlerEvent;
import com.marvin.bundle.web.event.RequestHandlerEvents;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;

public class RouterSubscriber implements SubscriberInterface<RequestHandlerEvent> {

    private final Router router;

    public RouterSubscriber(Router router) {
        this.router = router;
        System.out.println("new router subscriber");
    }
    
    public void onRequest(RequestHandlerEvent event){
        if(event instanceof GetResponseEvent) {
            HttpServletRequest request = event.getRequest();
            
            HashMap<String, Object> attributes = router.match(event.getRequest().getRequestURI());

//            RouteCollection routeCollection = router.getRouteCollection();
//            RequestMatcherInterface matcher = (RequestMatcherInterface) router.getMatcher();
//            
//            HashMap<String, Object> attributes = matcher.matchRequest(routeCollection, event.getRequest());
            
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
