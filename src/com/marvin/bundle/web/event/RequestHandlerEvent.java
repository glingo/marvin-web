package com.marvin.bundle.web.event;

import com.marvin.component.event.Event;
import com.marvin.bundle.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class RequestHandlerEvent extends Event {

    protected RequestHandler handler;
    
    private HttpServletRequest request;

    public RequestHandlerEvent(RequestHandler handler, HttpServletRequest request) {
        this.handler = handler;
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public RequestHandler getHandler() {
        return handler;
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }
    
}
