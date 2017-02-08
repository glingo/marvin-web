package com.marvin.bundle.web.controller.argumentResolver;

import com.marvin.component.kernel.controller.argument.ArgumentMetadata;
import com.marvin.component.kernel.controller.argument.ArgumentValueResolverInterface;
import com.marvin.component.util.ClassUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseValueResolver implements ArgumentValueResolverInterface<HttpServletRequest, HttpServletResponse>  {

    @Override
    public boolean support(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return ClassUtils.isAssignable(HttpServletResponse.class, argument.getType());
    }

    @Override
    public Object resolve(HttpServletRequest request, HttpServletResponse response, ArgumentMetadata argument) {
        return response;
    }
    
}
