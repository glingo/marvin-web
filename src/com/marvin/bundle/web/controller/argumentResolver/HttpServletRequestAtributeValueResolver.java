package com.marvin.bundle.web.controller.argumentResolver;

import com.marvin.bundle.framework.controller.argument.ArgumentMetadata;
import com.marvin.bundle.framework.controller.argument.ArgumentValueResolverInterface;
import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestAtributeValueResolver implements ArgumentValueResolverInterface<HttpServletRequest>  {

    @Override
    public boolean support(HttpServletRequest request, ArgumentMetadata argument) {
        return !argument.isIsVariadic() && request.getAttribute(argument.getName()) != null;
    }

    @Override
    public Object resolve(HttpServletRequest request, ArgumentMetadata argument) {
        return request.getAttribute(argument.getName());
    }
    
}
