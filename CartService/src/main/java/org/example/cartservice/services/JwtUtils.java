package org.example.cartservice.services;

import org.example.cartservice.exceptions.JwtAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;

public class JwtUtils {

    private JwtUtils() {
        // Private constructor to prevent instantiation
    }

    private static Map<String, Object> extractClaims(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            return jwtToken.getTokenAttributes();
        } else {
            throw new JwtAuthenticationException("Unauthorized: Invalid JWT token");
        }
    }

    public static Long extractUserIdClaim(Authentication authentication){
        // Optionally extract specific claims
        Map<String, Object> claims = JwtUtils.extractClaims(authentication);

        if (claims != null && claims.containsKey("userId")) {
            Object userIdClaim = claims.get("userId");
            if (userIdClaim instanceof Number) {
                return ((Number) userIdClaim).longValue();
            } else {
                throw new IllegalArgumentException("userId is not a valid number");
            }
        } else {
            throw new IllegalStateException("userId is missing");
        }
    }


}
