package com.kelley.medicationassistant.security.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * Handles JWT-related Authentification services.
 */
@Service
public class JwtService {
    private static final Logger jwtLogger = LoggerFactory.getLogger(JwtService.class);

    // 1 Day in MS - should be shorter in a production environment
    private final long EXPIRATION_TIME = 86400000;

    // Pulls secret key from application.properties - make sure it is defined
    @Value("${medicationassistant.jwtsecret}")
    private String jwtSecret;

    /**
     * Generates a JWT for the given user details, signed using secret key.
     *
     * @param userDetails the UserDetails object containing the user's information
     * @return JWT string representing the user's authentication token
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .signWith(key())
                .compact();
    }

    /**
     * Helper method used to generate a key from JwtSecret in application.properties
     *
     * @return Key object representing the secret key.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts username (subject) from a given JWT after verifying its signature with secret key.
     *
     * @param token the JWT token string from which username is to be extracted
     * @return String containing username (subject) from JWT
     */
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the integrity, expiration, and structure of a provided JWT token string
     *
     * @param token JWT token to be validated
     * @return boolean based on whether or not the token is valid
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);
            // Return true if valid (no exceptions thrown)
            return true;
        } catch (JwtException e) {
            // If there is an issue with the JWT, log and return false
            jwtLogger.error(e.getMessage());
            return false;
        }
    }
}
