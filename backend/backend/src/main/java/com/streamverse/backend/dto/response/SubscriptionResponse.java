package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionResponse {

    private Long id;

    private Long userId;
    private String userName;
    private String userEmail;

    private String plan;
    private Double amount;
    private String currency;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Boolean active;
    private String paymentStatus;

    private String razorpayOrderId;
    private String razorpayPaymentId;
}