package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.SubscriptionRequest;
import com.streamverse.backend.dto.response.SubscriptionResponse;
import com.streamverse.backend.entity.Subscription;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.SubscriptionRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    public SubscriptionResponse purchaseSubscription(
            String userEmail,
            SubscriptionRequest request
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String plan = request.getPlan().toUpperCase();

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = calculateEndDate(startDate, plan);
        Double amount = calculateAmount(plan);

        // Optional: old active subscription deactivate
        subscriptionRepository.findTopByUserIdAndActiveTrueOrderByEndDateDesc(user.getId())
                .ifPresent(oldSub -> {
                    oldSub.setActive(false);
                    subscriptionRepository.save(oldSub);
                });

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .amount(amount)
                .currency("INR")
                .startDate(startDate)
                .endDate(endDate)
                .active(true)
                .paymentStatus("SUCCESS")
                .razorpayOrderId("order_demo_" + UUID.randomUUID())
                .razorpayPaymentId("pay_demo_" + UUID.randomUUID())
                .build();

        Subscription saved = subscriptionRepository.save(subscription);

        return mapToResponse(saved);
    }

    @Override
    public SubscriptionResponse getMySubscription(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = subscriptionRepository
                .findTopByUserIdAndActiveTrueOrderByEndDateDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("No active subscription found"));

        if (subscription.getEndDate().isBefore(LocalDateTime.now())) {
            subscription.setActive(false);
            subscriptionRepository.save(subscription);
            throw new RuntimeException("Subscription expired");
        }

        return mapToResponse(subscription);
    }

    @Override
    public boolean hasActiveSubscription(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return subscriptionRepository.existsByUserIdAndActiveTrueAndEndDateAfter(
                user.getId(),
                LocalDateTime.now()
        );
    }

    private LocalDateTime calculateEndDate(LocalDateTime startDate, String plan) {

        return switch (plan) {
            case "MONTHLY" -> startDate.plusMonths(1);
            case "QUARTERLY" -> startDate.plusMonths(3);
            case "YEARLY" -> startDate.plusYears(1);
            default -> throw new RuntimeException("Invalid subscription plan");
        };
    }

    private Double calculateAmount(String plan) {

        return switch (plan) {
            case "MONTHLY" -> 299.0;
            case "QUARTERLY" -> 799.0;
            case "YEARLY" -> 1999.0;
            default -> throw new RuntimeException("Invalid subscription plan");
        };
    }

    private SubscriptionResponse mapToResponse(Subscription subscription) {

        User user = subscription.getUser();

        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .plan(subscription.getPlan())
                .amount(subscription.getAmount())
                .currency(subscription.getCurrency())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .active(subscription.getActive())
                .paymentStatus(subscription.getPaymentStatus())
                .razorpayOrderId(subscription.getRazorpayOrderId())
                .razorpayPaymentId(subscription.getRazorpayPaymentId())
                .build();
    }
}