package org.example.paymentservice.services;

import org.example.paymentservice.models.PaymentStatus;

public class PaymentStatusMapper {
    public static PaymentStatus mapStripeStatusToPaymentStatus(String stripeStatus) {
        switch (stripeStatus) {
            case "succeeded":
                return PaymentStatus.COMPLETED;
            case "requires_payment_method":
            case "requires_confirmation":
            case "requires_action":
                return PaymentStatus.PENDING;
            case "canceled":
            case "requires_capture":
                return PaymentStatus.FAILED;
            default:
                throw new IllegalArgumentException("Unexpected Stripe status: " + stripeStatus);
        }
    }
}
