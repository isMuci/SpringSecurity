package com.example.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.example.entity.User;
import com.example.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        log.info("doFilterInternal.authorization : {}", authorization);

        if(authorization!=null){
            String[] authorizationSplit = authorization.split(" ");
            String bearer=authorizationSplit[0];
            String token=authorizationSplit[1];
            if (!bearer.equals("Bearer") || token == null) {
                throw new AccessDeniedException("非法token");
            }
            Map<String, Claim> claims;
            try{
                claims = JWTUtil.tokenVerify(token);
            }catch (JWTVerificationException e){
                throw new AccessDeniedException("非法token");
            }
            User user = new User();
            user.setId(claims.get("id").asLong());
            user.setUsername(claims.get("username").asString());
            user.setPerms(new HashSet<>(claims.get("perms").asList(String.class)));
            log.info("tokenVerify.user : {}",user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }
}
