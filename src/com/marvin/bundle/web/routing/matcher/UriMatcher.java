package com.marvin.bundle.web.routing.matcher;

import com.marvin.component.routing.RouteCollection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public class UriMatcher extends RequestMatcher {

    @Override
    public boolean support(String uri) {
        return (uri != null);
    }

    @Override
    public HashMap<String, Object> matchRequest(RouteCollection collection, HttpServletRequest request) {
        return match(collection, request.getRequestURI());
    }

}
