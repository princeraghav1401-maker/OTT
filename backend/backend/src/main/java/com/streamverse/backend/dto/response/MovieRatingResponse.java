package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieRatingResponse {

    private Long movieId;
    private String movieTitle;
    private Double averageRating;
    private Long totalReviews;
}