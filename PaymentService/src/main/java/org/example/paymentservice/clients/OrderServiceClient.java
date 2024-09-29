package org.example.paymentservice.clients;

import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.OrderStatusDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceClient implements IOrderServiceClient {

    private final RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    public OrderServiceClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Order getOrderDetails(Long orderId, String token) {
        String url = orderServiceUrl + "/" + orderId.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderStatusDTO> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Order> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Order.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to get order details: " + responseEntity.getStatusCode());
        }
    }

    @Override
    public Order updateOrderWithPaymentId(Long orderId, Long paymentId){
        String url = orderServiceUrl + "/" + orderId.toString() + "/payment/" + paymentId;
        return restTemplate.getForObject(url, Order.class);
    }

    @Override
    public Order updateOrderStatus(String orderId, String status, String token) {
        String url = orderServiceUrl + "/" + orderId.toString() + "/status";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        OrderStatusDTO statusDTO = new OrderStatusDTO();
        statusDTO.setStatus(status);
        HttpEntity<OrderStatusDTO> requestEntity = new HttpEntity<>(statusDTO, headers);

        ResponseEntity<Order> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Order.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Failed to update order status: " + responseEntity.getStatusCode());
        }
    }

}
