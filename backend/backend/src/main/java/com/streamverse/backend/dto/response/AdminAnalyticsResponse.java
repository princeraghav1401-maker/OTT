package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminAnalyticsResponse {

    private Long totalUsers;
    private Long totalMovies;
    private Long totalSeries;
    private Long totalSeasons;
    private Long totalEpisodes;
    private Long totalReviews;

    private Long totalWatchlistItems;
    private Long totalFavorites;
    private Long totalNotifications;

    private Long totalSubscriptions;
    private Long activeSubscriptions;

    private Double totalRevenue;
}