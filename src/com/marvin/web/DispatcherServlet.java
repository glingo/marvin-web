package com.marvin.web;

import com.marvin.component.kernel.Kernel;

public class DispatcherServlet extends FrameworkServlet {

    public DispatcherServlet(Kernel kernel) {
        super(kernel);
        System.out.println("new dispatcher servlet");
    }

}
