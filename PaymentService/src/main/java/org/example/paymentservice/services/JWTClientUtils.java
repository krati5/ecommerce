package org.example.paymentservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTClientUtils {

    @Value("${auth.server.token.url}")
    private String tokenUrl;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;
    public String getAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);


        // Set up request body
        MultiValueMap<String, String>  body = new LinkedMultiValueMap();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);


        // Send POST request
        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = response.getBody();
            return responseBody != null ? responseBody.get("access_token") : null;
        } else {
            throw new RuntimeException("Failed to retrieve access token");
        }

    }
}
