package com.example.handler;

import com.alibaba.fastjson2.JSON;
import com.example.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = JWTUtil.token(authentication, 7L);

        response.setContentType("application/json;charset=UTF-8");
        HashMap<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("token",token);
        response.getWriter().write(JSON.toJSONString(tokenInfo));
    }
}
