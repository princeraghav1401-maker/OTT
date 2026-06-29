package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsResponse {

    private Long userId;
    private String name;
    private String email;

    private Long watchlistCount;
    private Long favoriteCount;
    private Long reviewCount;
    private Long watchedMoviesCount;

    private Boolean activeSubscription;
}