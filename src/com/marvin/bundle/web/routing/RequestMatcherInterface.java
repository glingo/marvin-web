package com.marvin.bundle.web.routing;

import com.marvin.component.routing.MatcherInterface;
import com.marvin.component.routing.RouteCollection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

public interface RequestMatcherInterface extends MatcherInterface {

    HashMap<String, Object> matchRequest(RouteCollection collection, HttpServletRequest request);
    
}
