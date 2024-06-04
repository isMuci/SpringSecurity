package com.example.config;

import com.example.filter.JWTFilter;
import com.example.filter.LoginFilter;
import com.example.filter.VerificationCodeFilter;
import com.example.handler.*;
import com.example.provider.IWebAuthenticationDetailsSource;
import com.example.token.IPersistentTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    IPersistentTokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers("/admin/api").hasAnyAuthority("admin:api")
                                .requestMatchers("/user/api").hasAnyAuthority("admin:api","user:api")
                                .requestMatchers("/app/api").permitAll()
                                .requestMatchers("/kaptcha/**").permitAll()

                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e->e.accessDeniedHandler(new LoginAccessDeniedHandler()))
                .formLogin(conf ->
                        conf
//                                .authenticationDetailsSource(new IWebAuthenticationDetailsSource())
                                .loginProcessingUrl("/login"))
                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
                .rememberMe(rm->
                        rm
                                .rememberMeParameter("rememberMe")
                                .rememberMeCookieName("rememberMe")
                                .tokenRepository(tokenRepository)
                                .key("myKey"))
                .sessionManagement(sm->
                        sm
//                                .invalidSessionUrl()
                                .invalidSessionStrategy(new InvalidSessionHandler())
                                .maximumSessions(1)
                                .sessionRegistry(sessionRegistry()))
                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .logout(logout->
                        logout
                                .invalidateHttpSession(true)
                                .deleteCookies("rememberMe")
//                                .logoutSuccessUrl()
                                .logoutSuccessHandler(new ILogoutSuccessHandler()))
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

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}