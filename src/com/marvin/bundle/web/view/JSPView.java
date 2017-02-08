package com.marvin.bundle.web.view;

import com.marvin.bundle.framework.mvc.view.View;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPView extends View<HttpServletRequest, HttpServletResponse> {
    
    private String url;

    public JSPView(String url) {
        this.url = url;
    }

    @Override
    public void render(HashMap<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(model != null) {
            model.forEach(request::setAttribute);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(getUrl());
        
        rd.include(request, response);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
