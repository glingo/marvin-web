package com.marvin.bundle.web.event;

import com.marvin.bundle.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class FinishRequestEvent extends RequestHandlerEvent {

    public FinishRequestEvent(RequestHandler handler, HttpServletRequest request) {
        super(handler, request);
    }

}
