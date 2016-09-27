package com.marvin.bundle.web.controller;

import com.marvin.component.kernel.controller.ContainerControllerResolver;
import com.marvin.component.kernel.controller.ControllerNameParser;
import com.marvin.component.kernel.controller.ControllerReference;
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
