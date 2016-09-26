package com.marvin.web.exception;

import java.util.HashMap;

public class NotFoundHttpException extends HttpException {

    public NotFoundHttpException(String message) {
        super(404, new HashMap<>(), message);
    }
    
}
