package com.marvin.bundle.web.routing.matcher;

import com.marvin.component.routing.RouteCollection;
import com.marvin.component.routing.matcher.PathMatcher;
import com.marvin.bundle.web.routing.RequestMatcherInterface;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public abstract class RequestMatcher extends PathMatcher implements RequestMatcherInterface {

    @Override
    public HashMap<String, Object> match(RouteCollection collection, String path) {
        return super.match(collection, path); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected HashMap<String, Object> matchCollection(String path, RouteCollection collection) {
        return super.matchCollection(path, collection); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean support(String matchable) {
        return super.support(matchable); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public abstract HashMap<String, Object> matchRequest(RouteCollection collection, HttpServletRequest request);
}
