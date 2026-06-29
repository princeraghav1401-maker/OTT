package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.PaymentRequest;
import com.streamverse.backend.dto.response.PaymentResponse;
import com.streamverse.backend.dto.response.SubscriptionResponse;

public interface PaymentService {

    PaymentResponse createOrder(String userEmail, PaymentRequest request);

    SubscriptionResponse verifyPayment(String userEmail, PaymentRequest request);
}