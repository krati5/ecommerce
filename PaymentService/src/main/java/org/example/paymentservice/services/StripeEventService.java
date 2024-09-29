package org.example.paymentservice.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import org.example.paymentservice.clients.IUserServiceClient;
import org.example.paymentservice.clients.OrderServiceClient;
import org.example.paymentservice.dtos.Order;
import org.example.paymentservice.dtos.User;
import org.example.paymentservice.exceptions.NotFoundException;
import org.example.paymentservice.models.Payment;
import org.example.paymentservice.models.PaymentStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;

import java.util.Optional;

@Service
public class StripeEventService {


    private final PaymentService paymentService;
    private final OrderServiceClient orderServiceClient;
    private final IUserServiceClient userServiceClient;
    private final EmailNotificationService emailNotificationService;
    private final String stripeSecretKey;
    private JWTClientUtils jwtClientUtils;

    public StripeEventService(PaymentService paymentService, OrderServiceClient orderServiceClient,
                              IUserServiceClient userServiceClient, EmailNotificationService emailNotificationService,
                              @Value("${stripe.secret_key}") String stripeSecretKey, JWTClientUtils jwtClientUtils) {
        this.paymentService = paymentService;
        this.orderServiceClient = orderServiceClient;
        this.userServiceClient = userServiceClient;
        this.emailNotificationService = emailNotificationService;
        this.stripeSecretKey = stripeSecretKey;
        this.jwtClientUtils = jwtClientUtils;
    }


    private PaymentIntent getPaymentIntent(Session session) throws StripeException {
        String paymentIntentId = session.getPaymentIntent();
        RequestOptions requestOptions = RequestOptions.builder().setApiKey(stripeSecretKey).build();
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId, requestOptions);
        return paymentIntent;

    }

    private void updatePaymentStatusFromStripe(Payment payment, String stripeStatus) {
        PaymentStatus paymentStatus = PaymentStatusMapper.mapStripeStatusToPaymentStatus(stripeStatus);
        payment.setPaymentStatus(paymentStatus);
        paymentService.savePayment(payment);
    }


    private void sendOrderStatusEmail(String customerEmail, String status, String orderId){
        String subject = "Payment "+status+" - Order #" + orderId;
        String body = "Your payment for Order #" + orderId + " has " + status;
        String topic = "sendEmail";
        emailNotificationService.sendEmail(customerEmail, subject, body, topic);
    }

    public void handlePaymentReceived(Session session) throws Exception {
        // Process the event
        // Step 1: Fetch reference id

        String referenceId = session.getPaymentLink();
        if(referenceId == null){
            throw new RuntimeException("Unable to get reference id");
        }


        // Step2 : Find payment object using reference id
        Optional<Payment> optionalPayment = paymentService.getPaymentByReferenceId(referenceId);
        if(optionalPayment.isEmpty()){
            throw new NotFoundException("Payment object with reference id "+ referenceId +" not found");
        }
        Payment payment = optionalPayment.get();

        // Step 3: update payment status
        PaymentIntent paymentIntent = getPaymentIntent(session);
        String paymentIntentStatus = paymentIntent.getStatus();
        // Extract the payment status
        if(paymentIntentStatus == null){
            throw new RuntimeException("Unable to get payment status ");
        }

        updatePaymentStatusFromStripe(payment, paymentIntentStatus);


        // Step 4: Update order status using order id in payment object
        String token = jwtClientUtils.getAccessToken();
        Long orderId = payment.getOrderId();
        orderServiceClient.updateOrderStatus(orderId.toString(), payment.getPaymentStatus().toString(), token);

        // Step 5: get user id from order object
        Order order = orderServiceClient.getOrderDetails(orderId, token);
        if(order == null){
            throw new RuntimeException("Unable to get order with id " + orderId);
        }

        // Step 6: get email from User object
        Long customerId = order.getCustomerId();
        User customer = userServiceClient.getUserDetails(customerId);
        if(customer == null){
            throw new RuntimeException("Unable to get customer with id " + customerId);
        }
        String customerEmail = customer.getEmail();
        System.out.println(customer);
        sendOrderStatusEmail(customerEmail, paymentIntentStatus, orderId.toString());

        }

}
