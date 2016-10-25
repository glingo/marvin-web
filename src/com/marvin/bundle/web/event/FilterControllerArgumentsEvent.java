package com.marvin.bundle.web.event;

import com.marvin.bundle.framework.controller.ControllerReference;
import com.marvin.bundle.web.RequestHandler;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class FilterControllerArgumentsEvent extends FilterControllerEvent {

    private List<Object> arguments;

    public FilterControllerArgumentsEvent(RequestHandler handler, ControllerReference controller, List<Object> arguments, HttpServletRequest request) {
        super(handler, controller, request);
        this.arguments = arguments;
    }

    public void setArguments(List<Object> arguments) {
        this.arguments = arguments;
    }

    public List<Object> getArguments() {
        return arguments;
    }
    
}
