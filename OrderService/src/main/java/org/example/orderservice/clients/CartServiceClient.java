package org.example.orderservice.clients;

import org.example.orderservice.dtos.CartDTO;
import org.example.orderservice.dtos.Product;
import org.example.orderservice.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CartServiceClient {
    private final RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;
    private final String cartServiceUrl;

    public CartServiceClient(RestTemplateBuilder restTemplateBuilder, @Value("${cart.service.url}") String cartServiceUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.cartServiceUrl = cartServiceUrl;
    }

    public CartDTO getCart(Long userId, String token) throws NotFoundException {
        String url = cartServiceUrl + "/cart/user";

        // Set the Authorization header with the Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the call to CartService with the token in the header
        ResponseEntity<CartDTO> response = restTemplate.exchange(url, HttpMethod.GET, entity, CartDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new NotFoundException("Cart not found for user with ID: " + userId);
        }

        CartDTO cartDTO = response.getBody();

        return cartDTO;
    }

    public void deleteCart(String token) {
        String url = cartServiceUrl + "/cart" ;

        // Set the Authorization header with the Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the call to CartService with the token in the header
        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

    }
}
