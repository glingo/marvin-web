package com.marvin.web;

import com.marvin.component.kernel.Kernel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrameworkServlet extends HttpServlet {

    private final Kernel kernel;

    public FrameworkServlet(Kernel kernel) {
        this.kernel = kernel;
    }
    
    private RequestHandler handler;
    
    private RequestHandler getRequestHandler(){
        if(handler == null) {
            this.handler = kernel.getContainer()
                    .get("request_handler", RequestHandler.class);
        }
        
        return this.handler;
    }

    @Override
    public void init() throws ServletException {
        kernel.boot();
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
        
        try {
            
            getServletContext().log(String.format("process request %s !", request.getRequestURI()));
            response = getRequestHandler().handle(request, response, true);
            response.flushBuffer();
        } catch (Exception ex) {
            
        }
    }

}
