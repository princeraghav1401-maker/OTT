package com.streamverse.backend.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.streamverse.backend.dto.request.PaymentRequest;
import com.streamverse.backend.dto.request.SubscriptionRequest;
import com.streamverse.backend.dto.response.PaymentResponse;
import com.streamverse.backend.dto.response.SubscriptionResponse;
import com.streamverse.backend.service.PaymentService;
import com.streamverse.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final SubscriptionService subscriptionService;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    @Override
    public PaymentResponse createOrder(String userEmail, PaymentRequest request) {
        try {
            String plan = request.getPlan().trim().toUpperCase();
            int amount = calculateAmount(plan);

            String keyId = razorpayKeyId.trim();
            String keySecret = razorpayKeySecret.trim();

            System.out.println("RAZORPAY KEY ID = [" + keyId + "]");
            System.out.println("RAZORPAY SECRET LENGTH = " + keySecret.length());

            RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "streamverse_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);

            return PaymentResponse.builder()
                    .keyId(keyId)
                    .orderId(order.get("id"))
                    .amount(amount * 100)
                    .currency("INR")
                    .plan(plan)
                    .status(order.get("status"))
                    .message("Razorpay order created successfully")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to create Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public SubscriptionResponse verifyPayment(String userEmail, PaymentRequest request) {
        try {
            String keySecret = razorpayKeySecret.trim();

            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", request.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
            attributes.put("razorpay_signature", request.getRazorpaySignature());

            boolean isValid = Utils.verifyPaymentSignature(attributes, keySecret);

            if (!isValid) {
                throw new RuntimeException("Invalid Razorpay payment signature");
            }

            SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
            subscriptionRequest.setPlan(request.getPlan().trim().toUpperCase());

            return subscriptionService.purchaseSubscription(userEmail, subscriptionRequest);

        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }

    private int calculateAmount(String plan) {
        return switch (plan) {
            case "MONTHLY" -> 299;
            case "QUARTERLY" -> 799;
            case "YEARLY" -> 1999;
            default -> throw new RuntimeException("Invalid subscription plan: " + plan);
        };
    }
}