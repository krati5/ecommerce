package org.example.paymentservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.models.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private Long paymentId;          // The ID of the newly created payment
    private Long orderId;            // The ID of the associated order
    private Double amount;           // The amount for the payment
    private PaymentStatus status;    // The current status of the payment

    // Convert Payment entity to PaymentResponseDto
    public static PaymentResponseDto toDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setPaymentId(payment.getId());
        dto.setOrderId(payment.getOrderId());
        dto.setAmount(payment.getAmount());
        dto.setStatus(payment.getPaymentStatus());

        return dto;
    }

    // Convert PaymentResponseDto to Payment entity
    public static Payment toEntity(PaymentResponseDto dto) {
        if (dto == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setId(dto.getPaymentId());
        payment.setOrderId(dto.getOrderId());
        payment.setAmount(dto.getAmount());
        payment.setPaymentStatus(dto.getStatus());

        // Note: We might not have all information needed to set every field in Payment here
        // For example, you might need to set `transaction` if required

        return payment;
    }
}