package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.PaymentRequest;
import com.streamverse.backend.dto.response.PaymentResponse;
import com.streamverse.backend.dto.response.SubscriptionResponse;
import com.streamverse.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestBody PaymentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.createOrder(authentication.getName(), request)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<SubscriptionResponse> verifyPayment(
            @RequestBody PaymentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                paymentService.verifyPayment(authentication.getName(), request)
        );
    }
}