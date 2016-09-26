package com.marvin.web.exception;

import java.util.HashMap;

public class HttpException extends RuntimeException {
    
    private final int statusCode;
    private final HashMap headers;

    public HttpException(int statusCode, HashMap headers, String message) {
        super(message);
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public HashMap getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
}
