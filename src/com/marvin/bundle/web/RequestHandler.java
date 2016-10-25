package com.marvin.bundle.web;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.marvin.component.event.EventDispatcher;
import com.marvin.bundle.framework.controller.argument.ArgumentResolver;
import com.marvin.bundle.framework.controller.ControllerReference;
import com.marvin.bundle.framework.controller.ControllerResolverInterface;
import com.marvin.component.util.ReflectionUtils;
import com.marvin.bundle.web.event.FilterControllerArgumentsEvent;
import com.marvin.bundle.web.event.FilterControllerEvent;
import com.marvin.bundle.web.event.FilterResponseEvent;
import com.marvin.bundle.web.event.GetResponseEvent;
import com.marvin.bundle.web.event.GetResponseForControllerResultEvent;
import com.marvin.bundle.web.event.GetResponseForExceptionEvent;
import com.marvin.bundle.web.event.RequestHandlerEvent;
import com.marvin.bundle.web.event.RequestHandlerEvents;
import com.marvin.bundle.web.exception.HttpException;
import com.marvin.bundle.web.exception.NotFoundHttpException;
import java.io.IOException;
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
    
    private void filterResponse(HttpServletRequest request, HttpServletResponse response) {
        
        FilterResponseEvent filterEvent = new FilterResponseEvent(this, request, response);
        this.dispatcher.dispatch(RequestHandlerEvents.RESPONSE, filterEvent);
        
        finishRequest(request);
        
        filterEvent.getResponse();
    }

    private void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
       
        if(response.isCommitted()) {
            // nothing else to do.
            return;
        }
        
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

        if(exception instanceof HttpException) {
            HttpException exc = (HttpException) exception;
            response.sendError(exc.getStatusCode(), exc.getMessage());
            // send header
//            response.addHeader(null, null);
        }
        
        filterResponse(request, response);
        
        // traitement a effectuer dans un getresponseforexception event listener.
        System.err.println("Oooups !");
        exception.printStackTrace();
//        exception.printStackTrace(response.getWriter());
    }
    
    public void handle(HttpServletRequest request, HttpServletResponse response, boolean capture) throws Exception {
        
        try {
           handlePart(request, response);
        } catch(Exception e) {
            if(!capture) {
                finishRequest(request);
                throw e;
            }
            
            handleException(e, request, response);
        }
    }
    
    private void handlePart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = new Date().getTime();
        
        // stack request
        requestStack.push(request);
        
        // useless, dans une Serlvet nous avons déja la response !
        // (changer le nom car c'est l'event qui declenche le requesthandler)
        
        // Dispatch event Request ( get response )
        GetResponseEvent responseEvent = new GetResponseEvent(this, request);
        this.dispatcher.dispatch(RequestHandlerEvents.REQUEST, responseEvent);
        
        if(responseEvent.hasResponse()) {
            filterResponse(request, responseEvent.getResponse());
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
        filterResponse(request, response);
        
        // flush here ?
        response.flushBuffer();
        
        long end = new Date().getTime();
        System.out.format("%s executed in %s ms\n", controller, end - start);

//        return response;
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
