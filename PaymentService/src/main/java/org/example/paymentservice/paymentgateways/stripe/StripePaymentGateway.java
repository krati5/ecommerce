package org.example.paymentservice.paymentgateways.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import org.example.paymentservice.dtos.PaymentLinkResponse;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.paymentgateways.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${stripe.secret_key}")
    private String stripeSecretKey;

    @Value("${order.service.url}")
    private String orderServiceUrl;
    @Override
    public PaymentLinkResponse generatePaymentLink(Long amount, Long orderId) throws StripeException {
        // Set your secret key. Remember to switch to your live secret key in production.
    // See your keys here: https://dashboard.stripe.com/apikeys

        Stripe.apiKey = stripeSecretKey;

        // Step 1: Create a Product
        ProductCreateParams productCreateParams =
                ProductCreateParams.builder().setName("Order #" + orderId).build();

        Product product = Product.create(productCreateParams);

        // Step 2: Create a Price associated with the Product
        PriceCreateParams priceCreateParams =
                PriceCreateParams.builder()
                        .setCurrency("inr")
                        .setUnitAmount(amount) // this amount is actually = actual_amount * 100
                        .setProduct(product.getId())
                        .build();

        Price price = Price.create(priceCreateParams);


        // Step 3: Create a Payment Link using the Price and the Payment Intent ID
        PaymentLinkCreateParams paymentLinkCreateParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )

                        .setAfterCompletion(PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                        .setUrl(orderServiceUrl+"/confirmation?orderId="+ orderId.toString())
                                        .build())
                                .build())
                        .build();

        PaymentLink paymentLink = PaymentLink.create(paymentLinkCreateParams);
        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse(paymentLink.getUrl(), paymentLink.getId());

        return paymentLinkResponse;
    }
}
