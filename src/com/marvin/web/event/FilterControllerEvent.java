package com.marvin.web.event;

import com.marvin.component.kernel.controller.ControllerReference;
import com.marvin.web.RequestHandler;
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
