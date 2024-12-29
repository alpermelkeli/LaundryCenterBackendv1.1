package org.alpermelkeli.security;

public class SecurityConstants {
    public static final long JWTExpiration = 60*6*10000; //60 minutes
    public static final long JWTRefreshExpiration = 4L *604800000; //28 days
    public static final long rateLimitToken = 4L; //try count
    public static final long rateLimitDuration = 15L; //minute
    public static final String jwtSecret = "secret"; //JWT secret key
    public static final String refreshTokenSecret = "refresh"; //Refresh token secret key
}
