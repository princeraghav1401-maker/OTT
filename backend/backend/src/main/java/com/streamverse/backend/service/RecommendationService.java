package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.RecommendationResponse;

public interface RecommendationService {

    RecommendationResponse getRecommendations(String userEmail);
}