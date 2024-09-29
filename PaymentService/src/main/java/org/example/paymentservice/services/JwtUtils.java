package org.example.paymentservice.services;

import org.example.paymentservice.exceptions.AuthenticationException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
            throw new AuthenticationException("Unauthorized: Invalid JWT token");
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


    // method to extract the token value
    public static String extractTokenValue(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            return jwtToken.getToken().getTokenValue();
        } else {
            throw new AuthenticationException("Unauthorized: Invalid JWT token");
        }
    }




}
