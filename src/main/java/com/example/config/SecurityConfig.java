package com.example.config;

import com.example.filter.LoginFilter;
import com.example.handler.*;
import com.example.service.UserDetailService;
import com.example.token.IPersistentTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    IPersistentTokenRepository tokenRepository;

    @Autowired
    private UserDetailService userDetailService;

    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers("/admin/api").hasAnyAuthority("admin")
                                .requestMatchers("/user/api").hasAnyAuthority("admin","user")
                                .requestMatchers("/app/api").permitAll()
                                .requestMatchers("/kaptcha/**").permitAll()

                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .anyRequest()
                                .authenticated()
//                                .access(new IAuthenticationManager())
                )
                .exceptionHandling(e->e.accessDeniedHandler(new LoginAccessDeniedHandler()))
//                .formLogin(conf ->
//                        conf
////                                .authenticationDetailsSource(new IWebAuthenticationDetailsSource())
//                                .loginProcessingUrl("/login"))
//                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
//                .rememberMe(rm->
//                        rm
//                                .rememberMeParameter("rememberMe")
//                                .rememberMeCookieName("rememberMe")
//                                .tokenRepository(tokenRepository)
//                                .key("myKey"))
//                .sessionManagement(sm->
//                        sm
////                                .invalidSessionUrl()
//                                .invalidSessionStrategy(new InvalidSessionHandler())
//                                .maximumSessions(1)
//                                .sessionRegistry(sessionRegistry()))
//                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
//                .logout(logout->
//                        logout
//                                .invalidateHttpSession(true)
//                                .deleteCookies("rememberMe")
////                                .logoutSuccessUrl()
//                                .logoutSuccessHandler(new ILogoutSuccessHandler()))
                .build();
    }

//    @Autowired
//    private AuthenticationConfiguration authenticationConfiguration;

//    @Bean
//    public LoginFilter loginFilter() throws Exception {
//        LoginFilter loginFilter=new LoginFilter();
//        loginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
//        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
//        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
//        return loginFilter;
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}