package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.SubscriptionRequest;
import com.streamverse.backend.dto.response.SubscriptionResponse;
import com.streamverse.backend.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/purchase")
    public ResponseEntity<SubscriptionResponse> purchaseSubscription(
            @Valid @RequestBody SubscriptionRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                subscriptionService.purchaseSubscription(email, request)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<SubscriptionResponse> getMySubscription(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                subscriptionService.getMySubscription(email)
        );
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkActiveSubscription(
            Authentication authentication
    ) {
        String email = authentication.getName();

        boolean active = subscriptionService.hasActiveSubscription(email);

        return ResponseEntity.ok(
                Map.of("active", active)
        );
    }
}