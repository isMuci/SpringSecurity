package com.example.filter;

import com.example.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        System.out.println(request.getRequestURI());
        log.info("JWTFilter.token = {}", token);
        if(token!=null){
            try {
                JWTUtil.tokenVerify(token);
            }catch (Exception e){
                throw new AccessDeniedException("非法token");
            }
        }
        filterChain.doFilter(request,response);
    }
}
