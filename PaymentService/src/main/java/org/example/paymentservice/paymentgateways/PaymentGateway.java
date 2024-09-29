package org.example.paymentservice.paymentgateways;

import com.stripe.model.PaymentLink;
import org.example.paymentservice.dtos.PaymentLinkResponse;

public interface PaymentGateway {

    PaymentLinkResponse generatePaymentLink(Long amount, Long orderId) throws Exception;
}
