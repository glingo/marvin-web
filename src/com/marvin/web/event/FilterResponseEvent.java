package com.marvin.web.event;

import com.marvin.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FilterResponseEvent extends RequestHandlerEvent {

    private HttpServletResponse response;
    
    public FilterResponseEvent(RequestHandler handler, HttpServletRequest request, HttpServletResponse response) {
        super(handler, request);
        this.response = response;
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
