package com.example.config;

import cn.hutool.json.JSONObject;
import com.example.filter.JWTFilter;
import com.example.handler.CustomAuthenticationEntryPoint;
import com.example.handler.LoginAccessDeniedHandler;
import com.example.manager.IAuthenticationManager;
import com.example.service.UserDetailService;
import com.example.token.IPersistentTokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    IPersistentTokenRepository tokenRepository;

    @Autowired
    IAuthenticationManager authenticationManager;

    @Autowired
    LoginAccessDeniedHandler loginAccessDeniedHandler;

    @Autowired
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JWTFilter jwtFilter;

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
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .exceptionHandling(e->e.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(loginAccessDeniedHandler))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/oauth/notify").permitAll()
                                .anyRequest()
//                                .authenticated()
                                .access(authenticationManager)

                )
//                .formLogin(conf ->
//                        conf
////                                .authenticationDetailsSource(new IWebAuthenticationDetailsSource())
//                                .loginProcessingUrl("/login"))
//                .rememberMe(rm->
//                        rm
////                                .rememberMeParameter("rememberMe")
////                                .rememberMeCookieName("rememberMe")
//                                .tokenRepository(tokenRepository))
//                .sessionManagement(sm->
//                        sm
////                                .invalidSessionUrl()
//                                .invalidSessionStrategy(new InvalidSessionHandler())
//                                .maximumSessions(1)
//                                .sessionRegistry(sessionRegistry()))
//                .addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new VerificationCodeFilter(), UsernamePasswordAuthenticationFilter.class)
//                .logout(logout->
//                        logout
//                                .invalidateHttpSession(true)
//                                .deleteCookies("rememberMe")
////                                .logoutSuccessUrl()
//                                .logoutSuccessHandler(new ILogoutSuccessHandler()))
                .oauth2Login(Customizer.withDefaults())

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
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.giteeClientRegistration());
    }

    // 配置giee的授权登录信息
    private ClientRegistration giteeClientRegistration() {
        return ClientRegistration.withRegistrationId("gitee")
                .clientId("9cecc02909cd71cc217c41fe5635755720544c385e352e698a38ff145318c169")
                .clientSecret("d57adb6d5ccd851134b7a490cb9c39313031b23111ed8e0a6ac3aaf50dbc4532")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权模式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8080/login/oauth2/code/gitee")
                // 授权范围，即创建应用时勾选的权限名
                .scope("user_info")
                .authorizationUri("https://gitee.com/oauth/authorize")
                .tokenUri("https://gitee.com/oauth/token")
                // 获取用户信息的接口
                .userInfoUri("https://gitee.com/api/v5/user")
                .userNameAttributeName("name")
                .build();
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