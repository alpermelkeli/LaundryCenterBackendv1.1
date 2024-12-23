package org.alpermelkeli.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.alpermelkeli.model.RefreshTokenEntity;
import org.alpermelkeli.security.SecurityConstants;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.sql.Ref;
import java.util.Date;
import java.util.UUID;


@Component
public class JWTGenerator {

    public String generateToken(Authentication authentication) {
        String email = authentication.getName(); // Get the email of the authenticated user
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.JWTExpiration);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.jwtSecret)
                .compact();
        return token;

    }

    public RefreshTokenEntity generateRefreshToken(Authentication authentication) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        String email = authentication.getName(); // Get the email of the authenticated user
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + SecurityConstants.JWTRefreshExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.refreshTokenSecret)
                .compact();

        refreshTokenEntity.setRefreshToken(refreshToken);

        refreshTokenEntity.setExpirationDate(expirationDate);

        return refreshTokenEntity;
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SecurityConstants.jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.jwtSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT has expired or incorrect.");
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.refreshTokenSecret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Refresh token has expired or incorrect.");
        }
    }
}
