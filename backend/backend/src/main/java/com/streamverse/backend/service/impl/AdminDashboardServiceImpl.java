package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.AdminDashboardResponse;
import com.streamverse.backend.repository.*;
import com.streamverse.backend.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    private final ReviewRepository reviewRepository;
    private final VideoFileRepository videoFileRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public AdminDashboardResponse getDashboardStats() {

        Long totalUsers = userRepository.count();
        Long totalMovies = movieRepository.count();
        Long totalSeries = seriesRepository.count();
        Long totalEpisodes = episodeRepository.count();
        Long totalReviews = reviewRepository.count();
        Long totalVideoFiles = videoFileRepository.count();

        Long activeSubscriptions = subscriptionRepository.countByActiveTrue();

        Double totalRevenue = subscriptionRepository.getTotalRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalMovies(totalMovies)
                .totalSeries(totalSeries)
                .totalEpisodes(totalEpisodes)
                .totalReviews(totalReviews)
                .totalVideoFiles(totalVideoFiles)
                .activeSubscriptions(activeSubscriptions)
                .totalRevenue(totalRevenue)
                .build();
    }
}