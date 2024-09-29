package org.example.paymentservice.paymentgateways.razorpay;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import org.example.paymentservice.dtos.PaymentLinkResponse;
import org.example.paymentservice.paymentgateways.PaymentGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import com.razorpay.RazorpayClient;


@Service
public class RazorpayPaymentGateway implements PaymentGateway {
    private RazorpayClient razorpayClient;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @Override
    public PaymentLinkResponse generatePaymentLink(Long orderId, Long amount) throws RazorpayException {

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount);
            paymentLinkRequest.put("currency","INR");
            paymentLinkRequest.put("accept_partial",false);
            paymentLinkRequest.put("expire_by", 1700502051);
            paymentLinkRequest.put("reference_id",orderId.toString());
            paymentLinkRequest.put("description","Payment for order #" + orderId.toString());
            JSONObject customer = new JSONObject();
            JSONObject notify = new JSONObject();
            notify.put("sms",false);
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("reminder_enable",true);
            paymentLinkRequest.put("callback_url",orderServiceUrl+"/confirmation?orderId="+ orderId.toString());
            paymentLinkRequest.put("callback_method","get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
            PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse(paymentLink.get("short_url"), paymentLink.get("id"));

            return paymentLinkResponse;

    }
}
