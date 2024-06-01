package com.example.config;

import com.example.handler.LoginFailureHandler;
import com.example.handler.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(conf ->
                        conf.requestMatchers("/login").permitAll()
                                .anyRequest().authenticated())
                .formLogin(conf ->
                        conf.loginProcessingUrl("/login")
                                .successHandler(new LoginSuccessHandler())
                                .failureHandler(new LoginFailureHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

}