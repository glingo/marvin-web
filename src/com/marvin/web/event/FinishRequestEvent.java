package com.marvin.web.event;

import com.marvin.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class FinishRequestEvent extends RequestHandlerEvent {

    public FinishRequestEvent(RequestHandler handler, HttpServletRequest request) {
        super(handler, request);
    }

}
