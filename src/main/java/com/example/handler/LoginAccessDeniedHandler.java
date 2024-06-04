package com.example.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class LoginAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("AccessDeniedException = "+accessDeniedException);
        accessDeniedException.printStackTrace();

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("异常："+accessDeniedException.getMessage());
    }
}
