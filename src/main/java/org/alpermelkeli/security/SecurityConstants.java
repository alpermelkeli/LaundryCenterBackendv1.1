package org.alpermelkeli.security;

public class SecurityConstants {
    public static final long JWTExpiration = 6*600000; //60 minutes
    public static final long JWTRefreshExpiration = 4L *604800000; //28 days
    public static final String jwtSecret = "secret";
    public static final String refreshTokenSecret = "refresh";
}
