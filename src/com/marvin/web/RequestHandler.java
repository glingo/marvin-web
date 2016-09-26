package com.marvin.web;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.marvin.component.event.EventDispatcher;
import com.marvin.component.kernel.controller.ArgumentResolver;
import com.marvin.component.kernel.controller.ControllerReference;
import com.marvin.component.kernel.controller.ControllerResolverInterface;
import com.marvin.component.util.ReflectionUtils;
import com.marvin.web.event.FilterControllerArgumentsEvent;
import com.marvin.web.event.FilterControllerEvent;
import com.marvin.web.event.FilterResponseEvent;
import com.marvin.web.event.GetResponseEvent;
import com.marvin.web.event.GetResponseForControllerResultEvent;
import com.marvin.web.event.GetResponseForExceptionEvent;
import com.marvin.web.event.RequestHandlerEvent;
import com.marvin.web.event.RequestHandlerEvents;
import com.marvin.web.exception.HttpException;
import com.marvin.web.exception.NotFoundHttpException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandler {
    
    private final Stack<HttpServletRequest> requestStack;
    private final ControllerResolverInterface ctrlResolver;
    private final EventDispatcher dispatcher;
    private final ArgumentResolver argsResolver;
    
    public RequestHandler(EventDispatcher dispatcher, ControllerResolverInterface ctrlResolver, ArgumentResolver argsResolver) {
        this.dispatcher = dispatcher;
        this.ctrlResolver = ctrlResolver;
        this.requestStack = new Stack<>();
        this.argsResolver = argsResolver;
    }
    
    public RequestHandler(EventDispatcher dispatcher, ControllerResolverInterface ctrlResolver, Stack<HttpServletRequest> requestStack, ArgumentResolver argsResolver) {
        this.dispatcher = dispatcher;
        this.ctrlResolver = ctrlResolver;
        this.requestStack = requestStack;
        this.argsResolver = argsResolver;
    }

    private void finishRequest(HttpServletRequest request) {
        this.dispatcher.dispatch(RequestHandlerEvents.FINISH_REQUEST, new RequestHandlerEvent(this, request));
        this.requestStack.pop();
    }
    
    private HttpServletResponse filterResponse(HttpServletRequest request, HttpServletResponse response) {
        
        FilterResponseEvent filterEvent = new FilterResponseEvent(this, request, response);
        this.dispatcher.dispatch(RequestHandlerEvents.RESPONSE, filterEvent);
        
        finishRequest(request);
        
        return filterEvent.getResponse();
    }

    private HttpServletResponse handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GetResponseForExceptionEvent exceptionEvent = new GetResponseForExceptionEvent(this, request, exception);
        this.dispatcher.dispatch(RequestHandlerEvents.EXCEPTION, exceptionEvent);
        
        exception = exceptionEvent.getException();
        
        // in servlet we always have a response !
//        if(!exceptionEvent.hasResponse()) {
//            finishRequest(request);
//            
//            throw new Exception(exception);
//        }
        
//        response = exceptionEvent.getResponse();
//        System.out.println("qsdq");
        if(exception instanceof HttpException) {
            HttpException exc = (HttpException) exception;
//            System.out.println("http excpetion");
//            response.setStatus(exc.getStatusCode());
            response.sendError(exc.getStatusCode(), exc.getMessage());
            // send header
//            response.addHeader(null, null);
        }

        try {
            return filterResponse(request, response);
        } catch(Exception e) {
            return response;
        }
    }
    
    public HttpServletResponse handle(HttpServletRequest request, HttpServletResponse response, boolean capture) throws Exception {
        
        try {
           return handlePart(request, response);
        } catch(Exception e) {
            
            if(!capture) {
                finishRequest(request);
                throw e;
            }
            
            return handleException(e, request, response);
        }
    }
    
    private HttpServletResponse handlePart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = new Date().getTime();
        
        // stack request
        requestStack.push(request);
        
        // Dispatch event Request ( get response )
        GetResponseEvent responseEvent = new GetResponseEvent(this, request);
        this.dispatcher.dispatch(RequestHandlerEvents.REQUEST, responseEvent);
        
        if(responseEvent.hasResponse()) {
             return filterResponse(request, responseEvent.getResponse());
        }
        
        request = responseEvent.getRequest();
        
        // Find controller via resolver
        ControllerReference controller = this.ctrlResolver.resolveController(request);

        if(controller == null){
            throw new NotFoundHttpException("No controller for " + request.getRequestURI());
        }
        
        // filter controller via event
        FilterControllerEvent filterEvent = new FilterControllerEvent(this, controller, request);
        this.dispatcher.dispatch(RequestHandlerEvents.CONTROLLER, filterEvent);
       
        controller = filterEvent.getController();
        
        // resolve arguments to pass
        List<Object> arguments = this.argsResolver.getArguments(request, controller);
        
        // filter controller arguments via event
        FilterControllerArgumentsEvent argsEvent = new FilterControllerArgumentsEvent(this, controller, arguments, request);
        this.dispatcher.dispatch(RequestHandlerEvents.CONTROLLER_ARGUMENTS, argsEvent);
        
        controller = argsEvent.getController();
        arguments = argsEvent.getArguments();
        
        // direct call controller
        Object controllerResponse = ReflectionUtils.invokeMethod(controller.getAction(), 
                controller.getHolder(), arguments.toArray());

        // typer la response
        if(!(controllerResponse instanceof String)) {
            
            // get response for controller result
            GetResponseForControllerResultEvent responseForControllerEvent = new GetResponseForControllerResultEvent(this, request, controllerResponse);
            this.dispatcher.dispatch(RequestHandlerEvents.RESPONSE, responseForControllerEvent);

            if(responseForControllerEvent.hasResponse()) {
                controllerResponse = responseForControllerEvent.getResponse();
            }
            
            if(!(controllerResponse instanceof String)) {
                // should it return a response ? 
                String msg = String.format("The Controller must return a String (%s given).", response);
                
                if(null == response) {
                    msg += " Did you forget to add a return statement in your controller ?";
                }
                
                throw new Exception(msg);
            }
            
        }
        
        // use a view resolver.
        response.getWriter().print(controllerResponse);
        
        // filter la response ( pop request )
        response = filterResponse(request, response);
        
        long end = new Date().getTime();
        System.out.format("%s executed in %s ms\n", controller, end - start);

        return response;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb
            .append("\n------")
            .append(super.toString())
            .append("------")
            .append("\n\t")
            .append(this.ctrlResolver)
            .append("\n\t")
            .append(this.dispatcher)
            .append("\n\t")
            .append(this.requestStack)
            .append("\n");
        
        return sb.toString();
    }

}
