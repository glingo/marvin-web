package com.marvin.bundle.web.controller;

import com.marvin.bundle.framework.mvc.controller.ContainerControllerResolver;
import com.marvin.bundle.framework.mvc.controller.ControllerNameParser;
import com.marvin.component.mvc.controller.ControllerReference;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestControllerResolver extends ContainerControllerResolver<HttpServletRequest> {

    public HttpServletRequestControllerResolver(ControllerNameParser parser) {
        super(parser);
    }
    
    @Override
    public ControllerReference resolve(HttpServletRequest request) throws Exception {
        Object controller = request.getAttribute("_controller");
        return castController(controller);
    }
}
