package com.marvin.bundle.web.controller;

import com.marvin.component.kernel.controller.ArgumentResolver;
import com.marvin.component.kernel.controller.ArgumentValueResolverInterface;
import com.marvin.component.kernel.controller.argumentResolver.DefaultValueResolver;
import com.marvin.bundle.web.controller.argumentResolver.HttpServletRequestAtributeValueResolver;
import com.marvin.bundle.web.controller.argumentResolver.HttpServletRequestValueResolver;
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
        
        return resolvers;
    } 
    
    
}
