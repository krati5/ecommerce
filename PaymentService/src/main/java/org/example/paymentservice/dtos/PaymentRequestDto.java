package org.example.paymentservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.models.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private Long orderId;    // Order ID for which the payment is being made
    private Double amount;   // Amount for the payment
    private PaymentStatus paymentStatus;

    public Payment toPayment() {
        Payment payment = new Payment();
        payment.setOrderId(this.orderId);
        payment.setAmount(this.amount);
        payment.setPaymentStatus(this.paymentStatus);
        return payment;
    }

    public static PaymentRequestDto fromPayment(Payment payment) {
        return new PaymentRequestDto(
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentStatus()
        );
    }
}
