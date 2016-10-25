package com.marvin.bundle.web.event;

import com.marvin.bundle.framework.controller.ControllerReference;
import com.marvin.bundle.web.RequestHandler;
import javax.servlet.http.HttpServletRequest;

public class FilterControllerEvent extends RequestHandlerEvent {
    
    private ControllerReference controller;

    public FilterControllerEvent(RequestHandler handler, ControllerReference controller, HttpServletRequest request) {
        super(handler, request);
        this.controller = controller;
    }

    public ControllerReference getController() {
        return controller;
    }

    public void setController(ControllerReference controller) {
        this.controller = controller;
    }
    
}
