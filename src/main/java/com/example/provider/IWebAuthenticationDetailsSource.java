package com.example.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class IWebAuthenticationDetailsSource extends WebAuthenticationDetailsSource {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new IWebAuthenticationDetails(context);
    }
}
