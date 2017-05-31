package com.marvin.bundle.web.view;

import com.marvin.component.mvc.view.ViewInterface;
import com.marvin.component.resolver.PathResolver;

public class URLViewResolver extends PathResolver<ViewInterface> {
    
    public URLViewResolver(String prefix, String sufix) {
        super(prefix, sufix);
    }

    @Override
    public ViewInterface doResolve(String name) throws Exception {
        return new JSPView(name);
    }
}
