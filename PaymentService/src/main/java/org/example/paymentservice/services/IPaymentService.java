package org.example.paymentservice.services;

import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.models.Payment;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    String createPaymentLink(Authentication authentication, Long orderId) throws Exception;

    Payment createPayment(Order order, String paymentLinkId, String paymentLinkUrl);

    Optional<Payment> getPaymentById(Long paymentId);

    Payment savePayment(Payment payment);

    List<Payment> getAllPayments();

    Optional<Payment> getPaymentByOrderId(Long orderId);

    Optional<Payment> getPaymentByReferenceId(String referenceId);
}
