package com.michael_gregory.blog_api.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.michael_gregory.blog_api.entity.User;
import com.michael_gregory.blog_api.service.UserService;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {    

    private UserService userServiceImpl;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CustomAuthenticationManager(UserService userServiceImpl, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userServiceImpl.getUser(authentication.getName());
        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect password.");
        }

        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword());
    }

    
}
