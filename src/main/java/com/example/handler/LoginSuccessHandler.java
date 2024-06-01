package com.example.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("loginOK");

        System.out.println("authentication.getAuthorities() =" + authentication.getCredentials());
        System.out.println("authentication.getAuthorities() =" + authentication.getPrincipal());
        System.out.println("authentication.getAuthorities() =" + authentication.getAuthorities());
    }
}
