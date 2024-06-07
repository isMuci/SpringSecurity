package com.example.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public String customerException(CustomException e) {
        return "customerException";
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exception(Exception e) {
        return "exception";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String RuntimeException(Exception e) {
        return "RuntimeException";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public String NoHandlerFoundException(Exception e) {
        return "NoHandlerFoundException";
    }
}
