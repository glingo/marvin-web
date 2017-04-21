package com.marvin.bundle.web;

import com.marvin.bundle.framework.handler.Handler;
import com.marvin.bundle.framework.mvc.ModelAndView;
import com.marvin.component.container.exception.ContainerException;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marvin.component.kernel.Kernel;

public class FrameworkServlet extends HttpServlet {

    private final Kernel kernel;
    private Handler handler;
    
    public FrameworkServlet(Kernel kernel) {
        this.kernel = kernel;
    }
    
    private Handler getRequestHandler() throws ContainerException {
        if(this.handler == null) {
            
            getServletContext().log("Trying to retrive the request handler.");
            this.handler = this.kernel.getContainer().get("request_handler", Handler.class);
        }
        
        return this.handler;
    }

    @Override
    public void init() throws ServletException {
        try {
            this.kernel.boot();
            this.handler = getRequestHandler();
        } catch (ContainerException ex) {
            getServletContext().log(null, ex);
        }
    }
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        getServletContext().log("Processing a request : " + request.getServletPath());
        
        try {
            this.handler.handle(request, response, true);
        } catch (Exception ex) {
             getServletContext().log("Exception : ", ex);
            if(!response.isCommitted()) {
                response.sendError(500, ex.getMessage());
            }
        }
    }

}
