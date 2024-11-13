package com.michael_gregory.blog_api.security;

public class SecurityConstants {
    public static final String SECRET_KEY = "%b1on;~8&KUi2c]z{9{K#<i/}`I}m~swJGj#BDWrNwk'2CaMlJ1!0BvCzM~,[o!"; //TODO confirm key is good //Your secret should always be strong (uppercase, lowercase, numbers, symbols) so that nobody can potentially decode the signature.
    public static final int TOKEN_EXPIRATION = 7200000; // 7200000 milliseconds = 7200 seconds = 2 hours.
    public static final String BEARER = "Bearer "; // Authorization : "Bearer " + Token 
    public static final String AUTHORIZATION = "Authorization"; // "Authorization" : Bearer Token
    public static final String REGISTER_PATH = "/api/public/user/register"; // Public path that clients can use to register.
    public static final String LOGIN_PATH = "/api/public/user/login"; // Public path that clients can use to login.
}