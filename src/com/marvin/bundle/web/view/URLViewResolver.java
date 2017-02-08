package com.marvin.bundle.web.view;

import com.marvin.bundle.framework.mvc.view.IView;
import com.marvin.bundle.framework.mvc.view.resolver.ViewResolver;

public class URLViewResolver extends ViewResolver {
    
    private String prefix;
    private String sufix;

    public URLViewResolver(String prefix, String sufix) {
        this.prefix = prefix;
        this.sufix = sufix;
    }

    @Override
    public IView resolveView(String name) throws Exception {
        name = this.prefix + name + this.sufix;
        return new JSPView(name);
    }

    public String getSufix() {
        return sufix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }
    
}
