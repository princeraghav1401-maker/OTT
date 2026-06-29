package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecommendationResponse {

    private String reason;

    private List<MovieResponse> recommendedMovies;
}