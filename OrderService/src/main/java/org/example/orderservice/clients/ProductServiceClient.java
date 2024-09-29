package org.example.orderservice.clients;

import org.example.orderservice.dtos.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient implements IProductServiceClient {

    private final RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;
    private final String productServiceUrl;

    public ProductServiceClient(RestTemplateBuilder restTemplateBuilder, @Value("${product.service.url}") String productServiceUrl) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.productServiceUrl = productServiceUrl;
    }

    @Override
    public Product getProductById(Long productId) {
        String url = productServiceUrl + "/products/" + productId.toString();
        return restTemplate.getForObject(url, Product.class);
    }

    @Override
    public Product updateProduct(Product product) {
        String url = productServiceUrl + "/products/" + product.getId();

        // Create HTTP headers (optional, but generally useful)
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create HTTP entity with the product and headers
        HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);

        // Send PUT request
        ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);

        // Check if update was successful
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            // Handle error response
            throw new RuntimeException("Failed to update product: " + response.getStatusCode());
        }
    }
}