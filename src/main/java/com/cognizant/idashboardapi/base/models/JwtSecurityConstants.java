package com.cognizant.idashboardapi.base.models;

public class JwtSecurityConstants {

    private JwtSecurityConstants() {
    }

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    /*Custom Token Constants*/
    public static final String BEARER_TEMPLATE = "Bearer %s";

    public static final String AUTH_TOKEN = "auth_token";
    public static final String EXPIRES_AT = "expiresAt";
    public static final String COLLECTOR_TOKEN = "collector_token";
    public static final String TOKEN_EXPIRES_AT = "tokenExpiresAt";
}