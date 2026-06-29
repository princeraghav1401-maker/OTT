package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.UserStatsResponse;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.*;
import com.streamverse.backend.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl implements UserStatsService {

    private final UserRepository userRepository;
    private final WatchlistRepository watchlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReviewRepository reviewRepository;
    private final WatchHistoryRepository watchHistoryRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public UserStatsResponse getMyStats(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long watchlistCount = watchlistRepository.countByUserId(user.getId());
        Long favoriteCount = favoriteRepository.countByUserId(user.getId());
        Long reviewCount = reviewRepository.countByUserIdAndDeletedFalse(user.getId());
        Long watchedMoviesCount = watchHistoryRepository.countByUserId(user.getId());

        Boolean activeSubscription =
                subscriptionRepository.existsByUserIdAndActiveTrueAndEndDateAfter(
                        user.getId(),
                        LocalDateTime.now()
                );

        return UserStatsResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .watchlistCount(watchlistCount)
                .favoriteCount(favoriteCount)
                .reviewCount(reviewCount)
                .watchedMoviesCount(watchedMoviesCount)
                .activeSubscription(activeSubscription)
                .build();
    }
}