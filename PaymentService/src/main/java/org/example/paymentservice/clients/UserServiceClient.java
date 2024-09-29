package org.example.paymentservice.clients;

import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient implements IUserServiceClient {

    private final RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public User getUserDetails(Long customerId) {
        String url = userServiceUrl + "/users/" + customerId.toString();
        return restTemplate.getForObject(url, User.class);
    }
}
