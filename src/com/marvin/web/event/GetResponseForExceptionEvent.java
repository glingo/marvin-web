package com.marvin.web.event;

import com.marvin.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class GetResponseForExceptionEvent extends GetResponseEvent {
    
    private Exception exception;

    public GetResponseForExceptionEvent(RequestHandler handler, HttpServletRequest request, Exception exception) {
        super(handler, request);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}