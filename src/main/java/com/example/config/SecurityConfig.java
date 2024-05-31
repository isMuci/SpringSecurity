package com.example.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(conf->
                conf.requestMatchers("/login").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(conf->
                conf
                        .loginProcessingUrl("/login")
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                response.setContentType("text/html;charset=UTF-8");
                                response.getWriter().write("loginOK");

                                System.out.println("authentication.getAuthorities() ="+authentication.getCredentials());
                                System.out.println("authentication.getAuthorities() ="+authentication.getPrincipal());
                                System.out.println("authentication.getAuthorities() ="+authentication.getAuthorities());

                            }
                        })
                        .failureHandler(new AuthenticationFailureHandler() {
                            @Override
                            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                response.setContentType("text/html;charset=UTF-8");
                                response.getWriter().write("loginERR");

                                exception.fillInStackTrace();
                            }
                        }));
        http.csrf(Customizer.withDefaults());
        http.cors(Customizer.withDefaults());

        return http.build();
    }

}