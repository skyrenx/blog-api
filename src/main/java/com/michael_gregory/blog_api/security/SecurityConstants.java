package com.michael_gregory.blog_api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    @Value("${security.secret-key}")
    private String secretKey;
    public static final int TOKEN_EXPIRATION = 7200000/*ms*/; // 2 hours
    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REGISTER_PATH = "/api/public/user/register";
    public static final String LOGIN_PATH = "/api/public/user/login";

    public String getSecretKey() {
        return secretKey;
    }
}