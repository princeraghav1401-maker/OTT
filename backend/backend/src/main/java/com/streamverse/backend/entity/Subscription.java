package com.streamverse.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who purchased subscription
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String plan; // MONTHLY, QUARTERLY, YEARLY

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 20)
    private String currency; // INR

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus; // SUCCESS, PENDING, FAILED

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }

        if (currency == null) {
            currency = "INR";
        }

        if (paymentStatus == null) {
            paymentStatus = "SUCCESS";
        }

        createdAt = LocalDateTime.now();
    }
}