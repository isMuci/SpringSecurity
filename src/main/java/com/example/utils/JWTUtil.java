package com.example.utils;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

@Slf4j
public class JWTUtil {
    public static final String secret = "muci";

    public static String token(Map<String,Object> map){
        return JWT
                .create()
                .withPayload(map)
                .sign(Algorithm.HMAC256(secret));
    }

    public static String token(Map<String,Object> map, Long time){
        return JWT
                .create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000L*60*time))
                .sign(Algorithm.HMAC256(secret));
    }

    public static Map<String, Claim> tokenVerify(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(secret)).build();
        Map<String, Claim> claims = jwtVerifier.verify(token).getClaims();
        log.info("tokenVerify.user : {}",claims);
        return claims;
    }
}
