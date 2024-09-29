package org.example.productservice.clients.authenticationclient;

import org.example.productservice.clients.authenticationclient.dtos.ValidateTokenRequestDto;
import org.example.productservice.clients.fakestoreapi.FakeStoreClient;
import org.example.productservice.clients.fakestoreapi.FakeStoreProductDto;
import org.example.productservice.clients.authenticationclient.dtos.ValidateTokenResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class AuthenticationClient {
    private static final Logger logger = LoggerFactory.getLogger(FakeStoreClient.class);
    private final RestTemplate restTemplate;

    private final String baseUrl;
    private RestTemplateBuilder restTemplateBuilder;

    public AuthenticationClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "http://localhost:9000/auth";
    }

    private String buildUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(baseUrl)
                .build()
                .toUriString();
    }

    private String buildUrlWithSubPath(String subPath) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(subPath)
                .build()
                .toUriString();
    }

    public Optional<ValidateTokenResponseDto> validateToken(String token, Long userId) {
            ValidateTokenRequestDto validateTokenRequestDto = new ValidateTokenRequestDto();
            validateTokenRequestDto.setToken(token);
            validateTokenRequestDto.setUserId(userId);

            String url = buildUrlWithSubPath("validate");
            ResponseEntity<ValidateTokenResponseDto> response = restTemplate.postForEntity(
                    url,
                    validateTokenRequestDto,
                    ValidateTokenResponseDto.class);

            ValidateTokenResponseDto validateTokenResponseDto = response.getBody();
            return Optional.ofNullable(validateTokenResponseDto);

    }

}
