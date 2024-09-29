package org.example.paymentservice.controllers;

import lombok.AllArgsConstructor;
import org.example.paymentservice.dtos.CreatePaymentLinkRequestDto;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.services.JWTClientUtils;
import org.example.paymentservice.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pay")
@AllArgsConstructor
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("/initiate")
    public String createPaymentLink(Authentication authentication, @RequestBody CreatePaymentLinkRequestDto requestDto) throws Exception {
        return paymentService.createPaymentLink(authentication, requestDto.getOrderId());
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentService.getPaymentById(id);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get payment by Order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        Optional<Payment> payment = paymentService.getPaymentByOrderId(orderId);
        return payment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get payment by reference id
    @GetMapping("/reference/{referenceId}")
    public ResponseEntity<Payment> getPaymentByReferenceId(@PathVariable String referenceId) {
        Optional<Payment> payment = paymentService.getPaymentByReferenceId(referenceId);

        return payment
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
}
