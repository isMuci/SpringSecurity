package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {
    public static final String assessTokenSecret = "muci";

    public static String accessToken(Map<String,Object> map){
        return JWT
                .create()
                .withPayload(map)
                .sign(Algorithm.HMAC256(assessTokenSecret));
    }

    public static String accessToken(Map<String,Object> map, Long time){
        return JWT
                .create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000L*60*time))
                .sign(Algorithm.HMAC256(assessTokenSecret));
    }

    public static Map<String, Claim> accessTokenVerify(String token){

        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(assessTokenSecret)).build();
        Map<String, Claim> claims = jwtVerifier.verify(token).getClaims();
        log.info("accessTokenVerify.user : {}",claims);
        return claims;
    }

    public static final String refreshTokenSecret = "refreshmuci";

    public static String refreshToken(Map<String,Object> map){
        return JWT
                .create()
                .withPayload(map)
                .sign(Algorithm.HMAC256(refreshTokenSecret));
    }

    public static String refreshToken(Map<String,Object> map, Long time){
        return JWT
                .create()
                .withPayload(map)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000L*60*time))
                .sign(Algorithm.HMAC256(refreshTokenSecret));
    }

    public static Map<String, Claim> refreshTokenVerify(String token){

        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(refreshTokenSecret)).build();
        Map<String, Claim> claims = jwtVerifier.verify(token).getClaims();
        log.info("refreshTokenVerify.user : {}",claims);
        return claims;
    }
}
