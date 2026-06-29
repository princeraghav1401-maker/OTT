package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findTopByUserIdAndActiveTrueOrderByEndDateDesc(Long userId);

    boolean existsByUserIdAndActiveTrueAndEndDateAfter(Long userId, LocalDateTime now);

    Long countByActiveTrue();

    @Query("""
            SELECT COALESCE(SUM(s.amount), 0)
            FROM Subscription s
            WHERE s.paymentStatus = 'SUCCESS'
            """)
    Double getTotalRevenue();
}