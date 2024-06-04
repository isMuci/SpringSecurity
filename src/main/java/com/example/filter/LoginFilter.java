package com.example.filter;

import cn.hutool.json.JSONUtil;
import com.example.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.BufferedReader;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        User user = obtainUser(request);
        UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword());

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private  User obtainUser(HttpServletRequest request) throws IOException {
        BufferedReader br= request.getReader();
        StringBuffer sb=new StringBuffer();
        String line=null;
        while ((line=br.readLine())!=null){
            sb.append(line);
        }
        System.out.println(sb);
        return JSONUtil.parseObj(sb.toString()).toBean(User.class);
    }
}
