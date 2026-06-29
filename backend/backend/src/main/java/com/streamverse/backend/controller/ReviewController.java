package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.ReviewRequest;
import com.streamverse.backend.dto.response.MovieRatingResponse;
import com.streamverse.backend.dto.response.ReviewResponse;
import com.streamverse.backend.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> addOrUpdateReview(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                reviewService.addOrUpdateReview(email, request)
        );
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewResponse>> getMovieReviews(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(
                reviewService.getMovieReviews(movieId)
        );
    }

    @GetMapping("/movie/{movieId}/rating")
    public ResponseEntity<MovieRatingResponse> getMovieRating(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(
                reviewService.getMovieRating(movieId)
        );
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        reviewService.deleteReview(email, reviewId);

        return ResponseEntity.ok("Review deleted successfully");
    }
}