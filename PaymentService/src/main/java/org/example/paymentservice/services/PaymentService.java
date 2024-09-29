package org.example.paymentservice.services;

import lombok.AllArgsConstructor;
import org.example.paymentservice.clients.IOrderServiceClient;
import org.example.paymentservice.dtos.CreatePaymentLinkRequestDto;
import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.PaymentLinkResponse;
import org.example.paymentservice.exceptions.NotFoundException;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.models.PaymentStatus;
import org.example.paymentservice.paymentgateways.PaymentGateway;
import org.example.paymentservice.paymentgateways.PaymentGatewayChooserStrategy;
import org.example.paymentservice.paymentgateways.stripe.StripePaymentGateway;
import org.example.paymentservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PaymentService {

    private PaymentGatewayChooserStrategy paymentGatewayChooserStrategy;

    private PaymentRepository paymentRepository;

    private final IOrderServiceClient orderServiceClient;

    public String createPaymentLink(Authentication authentication, Long orderId) throws Exception {

        PaymentGateway paymentGateway = paymentGatewayChooserStrategy.getBestPaymentGateway();

        // Step 1: Fetch order details from OrderClient
        // String orderId, String email, String phoneNumber, Long amount
        // //  Order order = orderService.getOrderDetails(orderId)
        String token = JwtUtils.extractTokenValue(authentication);
        Order order = orderServiceClient.getOrderDetails(orderId, token);

        if (order == null) {
            throw new NotFoundException("Order not found with id "+orderId);
        }

        // Step 2: Generate and get PaymentLink and PaymentLinkId
        Long amount = Math.round(order.getTotalAmount() * 100);
        PaymentLinkResponse paymentLinkResponse = paymentGateway.generatePaymentLink(amount, orderId);

        // Step 3: Create a new Payment record & Save payment to the database
        Payment payment = createPayment(order, paymentLinkResponse.getPaymentLinkId(), paymentLinkResponse.getPaymentLinkUrl());

        // Step 4: Update order with paymentId
        orderServiceClient.updateOrderWithPaymentId(orderId, payment.getId());

        return paymentLinkResponse.getPaymentLinkUrl();
    }

    public Payment createPayment(Order order,String paymentLinkId, String paymentLinkUrl){
        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus(PaymentStatus.PENDING); // Initial status
        payment.setPaymentLink(paymentLinkUrl);
        payment.setReferenceId(paymentLinkId);
        return savePayment(payment);
    }
    public  Optional<Payment> getPaymentById(Long paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public Optional<Payment> getPaymentByReferenceId(String referenceId) {
        return paymentRepository.findByReferenceId(referenceId);
    }
}
