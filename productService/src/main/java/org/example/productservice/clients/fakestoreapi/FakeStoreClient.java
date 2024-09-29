package org.example.productservice.clients.fakestoreapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.productservice.exceptions.NotFoundException;
import org.example.productservice.models.Product;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class FakeStoreClient {
    private static final Logger logger = LoggerFactory.getLogger(FakeStoreClient.class);
    private final RestTemplate restTemplate;

    private final String baseUrl;
    private RestTemplateBuilder restTemplateBuilder;

    public FakeStoreClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
        this.baseUrl = "https://fakestoreapi.com/products";
    }

    // Method to construct API URLs
    private String buildUrl() {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(baseUrl)
                .build()
                .toUriString();
    }

    private String buildUrlWithProductId(Long productId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(String.valueOf(productId))
                .build()
                .toUriString();
    }
    public List<FakeStoreProductDto> getAllProducts() {

        try {
            String url = buildUrl();
            ResponseEntity<FakeStoreProductDto[]> l = restTemplate.getForEntity(
                    url,
                    FakeStoreProductDto[].class
            );

            return Arrays.asList(l.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error fetching all products: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Error fetching all products: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<FakeStoreProductDto> getSingleProduct(Long productId) {

        try {
            String url = buildUrlWithProductId(productId);
            ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(
                    url,
                    FakeStoreProductDto.class,
                    productId);

            FakeStoreProductDto fakeStoreProductDto = response.getBody();
            return Optional.ofNullable(fakeStoreProductDto);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("HTTP error fetching product with id {}: {}", productId, e.getMessage(), e);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching product with id {}: {}", productId, e.getMessage(), e);
            return Optional.empty();
        }

    }

    public ResponseEntity<FakeStoreProductDto> addNewProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = FakeStoreProductDto.fromProduct(product);
        try {
            String url = buildUrl();
            ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(
                    url,
                    fakeStoreProductDto,
                    FakeStoreProductDto.class
            );

            return response;
        } catch (Exception e) {
            logger.error("Error adding product: {}", e.getMessage(), e);
            throw e;
        }
    }


    /*
    Product object has only those fields filled which need to be updated.
    Everything else is null
     */
    public ResponseEntity<FakeStoreProductDto> updateProduct(Long productId, Product product) {
         FakeStoreProductDto fakeStoreProductDto = FakeStoreProductDto.fromProduct(product);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<FakeStoreProductDto> requestEntity = new HttpEntity<>(fakeStoreProductDto, headers);

            String url = buildUrlWithProductId(productId);
            ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, FakeStoreProductDto.class);
            return response;
        } catch (Exception e) {
            logger.error("Error updating product with id {}: {}", productId, e.getMessage(), e);
            throw e;
        }
    }

    public ResponseEntity<FakeStoreProductDto> replaceProduct(Long productId, Product product) {
        FakeStoreProductDto fakeStoreProductDto = FakeStoreProductDto.fromProduct(product);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<FakeStoreProductDto> requestEntity = new HttpEntity<>(fakeStoreProductDto, headers);

            String url = buildUrlWithProductId(productId);
            ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, FakeStoreProductDto.class);
            return response;
        } catch (Exception e) {
            logger.error("Error replacing product with id {}: {}", productId, e.getMessage(), e);
            throw e;
        }
    }

    public ResponseEntity<FakeStoreProductDto> deleteProduct(Long productId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<FakeStoreProductDto> requestEntity = new HttpEntity<>(headers);

            String url = buildUrlWithProductId(productId);
            ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, FakeStoreProductDto.class);
            return response;
        } catch (Exception e) {
            logger.error("Error deleting product with id {}: {}", productId, e.getMessage(), e);
            throw e;
        }
    }
}
