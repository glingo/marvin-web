package com.marvin.bundle.web.controller.argumentResolver;

import com.marvin.component.mvc.controller.argument.ArgumentMetadata;
import com.marvin.component.mvc.controller.argument.ArgumentValueResolverInterface;
import com.marvin.component.util.ClassUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestValueResolver implements ArgumentValueResolverInterface<HttpServletRequest, HttpServletResponse>  {

    @Override
    public boolean support(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return ClassUtils.isAssignable(HttpServletRequest.class, argument.getType());
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return request;
    }
    
}
