package com.marvin.bundle.web.controller.argumentResolver;

import com.marvin.component.mvc.controller.argument.ArgumentMetadata;
import com.marvin.component.mvc.controller.argument.ArgumentValueResolverInterface;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestAtributeValueResolver implements ArgumentValueResolverInterface<HttpServletRequest, HttpServletResponse>  {

    @Override
    public boolean support(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return !argument.isIsVariadic() && request.getAttribute(argument.getName()) != null;
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return request.getAttribute(argument.getName());
    }
    
}
