package com.example.utils;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

public class JWTUtil {
    public static final String secret = "muci";

    public static String token(Authentication authentication){
        return JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis()+1000L*60*60*24*7))
                .withAudience(JSON.toJSONString(authentication))
                .sign(Algorithm.HMAC256(secret));
    }

    public static String token(Authentication authentication,Long time){
        return JWT
                .create()
                .withExpiresAt(new Date(System.currentTimeMillis()+1000L*60*time))
                .withAudience(JSON.toJSONString(authentication))
                .sign(Algorithm.HMAC256(secret));
    }

    public static void tokenVerify(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(secret)).build();
        jwtVerifier.verify(token);

        JWT.decode(token).getExpiresAt();
        String json = JWT.decode(token).getAudience().get(0);

        JWTAuthentication jwtAuthentication=JSON.parseObject(json,JWTAuthentication.class);

        System.out.println(json);

        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        System.out.println(authentication.isAuthenticated());

    }
}
