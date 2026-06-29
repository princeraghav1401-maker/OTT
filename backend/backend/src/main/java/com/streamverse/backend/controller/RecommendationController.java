package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.RecommendationResponse;
import com.streamverse.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<RecommendationResponse> getRecommendations(
            Authentication authentication
    ) {
        String email = null;

        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }

        return ResponseEntity.ok(
                recommendationService.getRecommendations(email)
        );
    }
}