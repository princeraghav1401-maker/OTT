package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminDashboardResponse {

    private Long totalUsers;
    private Long totalMovies;
    private Long totalSeries;
    private Long totalEpisodes;
    private Long totalReviews;
    private Long totalVideoFiles;

    private Long activeSubscriptions;
    private Double totalRevenue;
}