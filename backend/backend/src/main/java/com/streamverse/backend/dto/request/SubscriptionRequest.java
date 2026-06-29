package com.streamverse.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubscriptionRequest {

    @NotBlank
    private String plan; // MONTHLY, QUARTERLY, YEARLY
}