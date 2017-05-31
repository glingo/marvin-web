package com.marvin.bundle.web.view;

import com.marvin.bundle.framework.mvc.Handler;
import com.marvin.component.mvc.view.View;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSPView extends View<HttpServletRequest, HttpServletResponse> {
    
    public JSPView(String name) {
        super(name);
    }

    @Override
    public void render(
            Handler<HttpServletRequest, HttpServletResponse> handler, 
            HashMap<String, Object> model, 
            HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
        if(model != null) {
            model.forEach(request::setAttribute);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(this.name);
        
        rd.include(request, response);
    }

    @Override
    public void load() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
