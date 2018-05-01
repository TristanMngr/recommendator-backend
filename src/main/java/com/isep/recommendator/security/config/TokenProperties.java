package com.isep.recommendator.security.config;

public class TokenProperties {

    private static final String SECRET = "BRUH";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String SIGN_UP_URL = "/users/register";
    private static final String AUTH_URL = "/users/auth";

    public static String getSecret() {
        return SECRET;
    }

    public static long getExpirationTime() {
        return EXPIRATION_TIME;
    }

    public static String getHeaderString() {
        return HEADER_STRING;
    }

    public static String getSignUpUrl() {
        return SIGN_UP_URL;
    }

    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public static String getAuthUrl() {
        return AUTH_URL;
    }
}
