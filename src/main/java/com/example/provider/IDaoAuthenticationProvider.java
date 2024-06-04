package com.example.provider;

import com.example.exception.VerificationCodeException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
public class IDaoAuthenticationProvider extends DaoAuthenticationProvider {

    public IDaoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        super.setUserDetailsService(userDetailsService);
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        IWebAuthenticationDetails details=(IWebAuthenticationDetails)authentication.getDetails();

        if(!details.isVERIFICATION_CODE_CORRECT()){
            throw new VerificationCodeException();
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
