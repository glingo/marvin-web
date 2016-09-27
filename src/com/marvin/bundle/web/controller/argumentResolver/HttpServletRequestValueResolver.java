package com.marvin.bundle.web.controller.argumentResolver;

import com.marvin.component.kernel.controller.ArgumentMetadata;
import com.marvin.component.kernel.controller.ArgumentValueResolverInterface;
import com.marvin.component.util.ClassUtils;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestValueResolver implements ArgumentValueResolverInterface<HttpServletRequest>  {

    @Override
    public boolean support(HttpServletRequest request, ArgumentMetadata argument) {
        return ClassUtils.isAssignable(HttpServletRequest.class, argument.getType());
    }

    @Override
    public Object resolve(HttpServletRequest request, ArgumentMetadata argument) {
        return request;
    }
    
}
