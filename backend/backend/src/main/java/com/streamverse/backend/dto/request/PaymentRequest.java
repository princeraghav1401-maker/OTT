package com.streamverse.backend.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {

    private String plan; // MONTHLY, QUARTERLY, YEARLY

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}