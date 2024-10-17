package org.example.paymentservice.clients;

import org.example.paymentservice.dtos.Order;

public interface IOrderServiceClient {
    Order getOrderDetails(Long orderId, String token) ;

    Order updateOrderWithPaymentId(Long orderId, Long id, String token);


    Order updateOrderStatus(String orderId, String status, String token) throws Exception;
}
