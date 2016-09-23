package com.marvin.web.event;

import com.marvin.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class GetResponseForControllerResultEvent extends RequestHandlerEvent {
    
    private Object response;

    public GetResponseForControllerResultEvent(RequestHandler handler, HttpServletRequest request, Object response) {
        super(handler, request);
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
        this.stopEventPropagation();
    }
    
    public boolean hasResponse(){
        return this.response != null;
    }
}
