package com.marvin.bundle.web.controller;

import com.marvin.bundle.framework.mvc.controller.argument.ArgumentResolver;
import com.marvin.bundle.framework.mvc.controller.argument.ArgumentValueResolverInterface;
import com.marvin.bundle.framework.mvc.controller.argument.argumentResolver.DefaultValueResolver;
import com.marvin.bundle.web.controller.argumentResolver.HttpServletRequestAtributeValueResolver;
import com.marvin.bundle.web.controller.argumentResolver.HttpServletRequestValueResolver;
import com.marvin.bundle.web.controller.argumentResolver.HttpServletResponseValueResolver;
import java.util.ArrayList;
import java.util.List;

public class HttpServletRequestArgumentResolver extends ArgumentResolver {

    public HttpServletRequestArgumentResolver() {
        super(getDefaultResolvers());
    }
    
    public HttpServletRequestArgumentResolver(List<ArgumentValueResolverInterface> resolvers) {
        super(resolvers);
    }
    
    public static List<ArgumentValueResolverInterface> getDefaultResolvers(){
        List<ArgumentValueResolverInterface> resolvers = new ArrayList<>();
        
        resolvers.add(new DefaultValueResolver());
        resolvers.add(new HttpServletRequestAtributeValueResolver());
        resolvers.add(new HttpServletRequestValueResolver());
        resolvers.add(new HttpServletResponseValueResolver());
        
        return resolvers;
    } 
    
    
}
