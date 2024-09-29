package org.example.paymentservice.paymentgateways;


import org.example.paymentservice.paymentgateways.razorpay.RazorpayPaymentGateway;
import org.example.paymentservice.paymentgateways.stripe.StripePaymentGateway;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayChooserStrategy {
    private RazorpayPaymentGateway razorpayPaymentGateway;
    private StripePaymentGateway stripePaymentGateway;

    public PaymentGatewayChooserStrategy(
            RazorpayPaymentGateway razorpayPaymentGateway,
            StripePaymentGateway stripePaymentGateway
    ) {
        this.razorpayPaymentGateway = razorpayPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public PaymentGateway getBestPaymentGateway() {
//        int randomInt = new Random().nextInt();
//
//        if (randomInt % 2 == 0) {
//            return razorpayPaymentGateway;
//        }
//
        return stripePaymentGateway;
    }


}