package org.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Long id;
    private Long customerId;
    private Double totalAmount;
    private Long PaymentId;
}
