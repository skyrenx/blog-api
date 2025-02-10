package com.michael_gregory.blog_api.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.michael_gregory.blog_api.entity.users.Authority;
import com.michael_gregory.blog_api.entity.users.User;
import com.michael_gregory.blog_api.service.AuthorityService;
import com.michael_gregory.blog_api.service.UserService;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {    

    private UserService userServiceImpl;
    private AuthorityService authorityService;
    private PasswordEncoder bCryptPasswordEncoder;

    public CustomAuthenticationManager(@Lazy UserService userServiceImpl, AuthorityService authorityService, @Lazy PasswordEncoder bCryptPasswordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.authorityService = authorityService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userServiceImpl.getUser(authentication.getName());
        if (!bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("You provided an incorrect password.");
        }
        // Retrieve the user's authorities (roles) from the database
        List<Authority> authorities = authorityService.getAuthorities(user.getUsername());
        // Convert Authority entities to a list of GrantedAuthority
        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(authentication.getName(), user.getPassword(), grantedAuthorities);
    }
    
}
