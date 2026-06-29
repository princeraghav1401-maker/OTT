package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.SubscriptionRequest;
import com.streamverse.backend.dto.response.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse purchaseSubscription(String userEmail, SubscriptionRequest request);

    SubscriptionResponse getMySubscription(String userEmail);

    boolean hasActiveSubscription(String userEmail);
}