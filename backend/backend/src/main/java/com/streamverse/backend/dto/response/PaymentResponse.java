package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String keyId;
    private String orderId;
    private Integer amount;
    private String currency;
    private String plan;
    private String status;
    private String message;
}