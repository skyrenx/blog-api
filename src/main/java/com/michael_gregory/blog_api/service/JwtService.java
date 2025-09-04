package com.michael_gregory.blog_api.service;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.michael_gregory.blog_api.security.SecurityConstants;

@Service
public class JwtService {
    private final SecurityConstants securityConstants;
    @Value("${security.jwt-cookie.secure}")
    private Boolean secureFlag;
    @Value("${security.jwt-cookie.sameSite}")
    private String sameSiteFlag;

    public JwtService(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    public String generateToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(securityConstants.getSecretKey()));
    }

    public ResponseCookie buildHttpOnlyCookie(String token) {
        return ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .secure(secureFlag) // true in prod with HTTPS
                .path("/")
                .maxAge(Duration.ofHours(1)) //TODO confirm 1 hour expiration
                .sameSite(sameSiteFlag) //TODO set to "String" when front and backend are on same domain
                .build();
    }
}
