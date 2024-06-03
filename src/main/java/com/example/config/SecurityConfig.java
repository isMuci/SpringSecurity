package com.example.config;

import com.example.handler.LoginFailureHandler;
import com.example.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

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

                                .requestMatchers("/login").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e->e.accessDeniedPage("/noAuth"))
                .formLogin(conf ->
                        conf.loginProcessingUrl("/login")
                                .successHandler(new LoginSuccessHandler())
                                .failureHandler(new LoginFailureHandler()))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager(){
        JdbcUserDetailsManager userDetailsManager=new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);

        UserDetails user1= User.withUsername("admin").password("123456").authorities("admin:api","user:api").build();
        UserDetails user2= User.withUsername("user").password("123456").authorities("user:api").build();



        if(!userDetailsManager.userExists("admin")||!userDetailsManager.userExists("user")){
            System.out.println("创建用户中");
            userDetailsManager.createUser(user1);
            userDetailsManager.createUser(user2);
        }

        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}