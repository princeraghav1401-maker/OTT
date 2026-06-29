package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.AdminAnalyticsResponse;
import com.streamverse.backend.repository.*;
import com.streamverse.backend.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final ReviewRepository reviewRepository;
    private final WatchlistRepository watchlistRepository;
    private final FavoriteRepository favoriteRepository;
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public AdminAnalyticsResponse getAnalytics() {

        Long totalSubscriptions = subscriptionRepository.count();
        Long activeSubscriptions = subscriptionRepository.countByActiveTrue();
        Double totalRevenue = subscriptionRepository.getTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        return AdminAnalyticsResponse.builder()
                .totalUsers(userRepository.count())
                .totalMovies(movieRepository.count())
                .totalSeries(seriesRepository.count())
                .totalSeasons(seasonRepository.count())
                .totalEpisodes(episodeRepository.count())
                .totalReviews(reviewRepository.count())

                .totalWatchlistItems(watchlistRepository.count())
                .totalFavorites(favoriteRepository.count())
                .totalNotifications(notificationRepository.count())

                .totalSubscriptions(totalSubscriptions)
                .activeSubscriptions(activeSubscriptions)
                .totalRevenue(totalRevenue)
                .build();
    }
}