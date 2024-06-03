package com.example.config;

import com.example.filter.LoginFilter;
import com.example.handler.LoginAccessDeniedHandler;
import com.example.handler.LoginFailureHandler;
import com.example.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers("/admin/api").hasAnyAuthority("admin:api")
                                .requestMatchers("/user/api").hasAnyAuthority("admin:api","user:api")
                                .requestMatchers("/app/api").permitAll()
                                .requestMatchers("/kaptcha/**").permitAll()

                                .requestMatchers("/login").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e->e.accessDeniedHandler(new LoginAccessDeniedHandler()))
                .formLogin(conf ->
                        conf.loginProcessingUrl("/login")
                                )
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter=new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return loginFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}