package org.example.paymentservice.controllers.webhooks;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.example.paymentservice.clients.*;
import org.example.paymentservice.dtos.EmailNotification;
import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.PaymentLinkResponse;
import org.example.paymentservice.dtos.User;
import org.example.paymentservice.exceptions.NotFoundException;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.models.PaymentStatus;
import org.example.paymentservice.services.EmailNotificationService;
import org.example.paymentservice.services.PaymentService;
import org.example.paymentservice.services.PaymentStatusMapper;
import org.example.paymentservice.services.StripeEventService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhooks/stripe/")
public class StripeWebhookController {

    private final StripeEventService stripeEventService;

    // Secret key used to verify the Stripe webhook signature
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;


    public StripeWebhookController(StripeEventService stripeEventService) {
        this.stripeEventService = stripeEventService;
    }


    @PostMapping
    public void receiveWebhookEvent(HttpServletRequest request) throws Exception {
        System.out.println("Waiting");
        Event event = null;
        String body = request.getReader().lines().collect(Collectors.joining("\n"));
        try {
            String sigHeader = request.getHeader("Stripe-Signature");
            event = Webhook.constructEvent(body, sigHeader, endpointSecret);
        } catch (Exception e) {
            System.out.println("Error while verifying stripe event signature" + e);
            return;
        }


        // 1. Read the event details and handle different event types
        switch (event.getType()) {
            case "payment_link.created":
                handlePaymentLinkCreated(event);
                break;
            case "payment_link.updated":
                handlePaymentLinkUpdated(event);
                break;
//            case "payment_intent.succeeded":
                case "checkout.session.completed":
                handlePaymentReceived(event);
                break;
            default:
                System.out.println("Unhandled event type: " + event.getType());
                break;
        }

    }

    private void handlePaymentLinkCreated(Event event) {
        // Extract the PaymentLink object from the event
        PaymentLink paymentLink = (PaymentLink) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (paymentLink != null) {
            // Process the payment link creation event
            System.out.println("Payment link created: " + paymentLink.getId());
        }
    }

    private void handlePaymentLinkUpdated(Event event) {
        // Extract the PaymentLink object from the event
        PaymentLink paymentLink = (PaymentLink) event.getDataObjectDeserializer()
                .getObject().orElse(null);
        if (paymentLink != null) {
            // Process the payment link update event
            System.out.println("Payment link updated: " + paymentLink.getId());
        }
    }


    private void handlePaymentReceived(Event event) throws Exception {
        // Extract the session from the event
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        if (session != null) {
            stripeEventService.handlePaymentReceived(session);
        }
    }
}