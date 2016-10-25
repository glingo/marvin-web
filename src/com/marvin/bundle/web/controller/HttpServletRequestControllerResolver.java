package com.marvin.bundle.web.controller;

import com.marvin.bundle.framework.controller.ContainerControllerResolver;
import com.marvin.bundle.framework.controller.ControllerNameParser;
import com.marvin.bundle.framework.controller.ControllerReference;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestControllerResolver extends ContainerControllerResolver<HttpServletRequest> {

    public HttpServletRequestControllerResolver(ControllerNameParser parser) {
        super(parser);
    }
    
    @Override
    public ControllerReference resolveController(HttpServletRequest request) throws Exception {
        Object controller = request.getAttribute("_controller");
        return castController(controller);
    }
}
