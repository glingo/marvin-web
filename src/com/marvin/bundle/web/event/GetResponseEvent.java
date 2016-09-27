package com.marvin.bundle.web.event;

import com.marvin.bundle.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetResponseEvent extends RequestHandlerEvent {

    private HttpServletResponse response;
    
    public GetResponseEvent(RequestHandler handler, HttpServletRequest request) {
        super(handler, request);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
        this.stopEventPropagation();
    }
    
    public boolean hasResponse() {
        return this.response != null;
    }
    
}
