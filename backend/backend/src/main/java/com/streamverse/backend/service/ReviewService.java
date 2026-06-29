package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.ReviewRequest;
import com.streamverse.backend.dto.response.MovieRatingResponse;
import com.streamverse.backend.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {

    ReviewResponse addOrUpdateReview(String userEmail, ReviewRequest request);

    List<ReviewResponse> getMovieReviews(Long movieId);

    MovieRatingResponse getMovieRating(Long movieId);

    void deleteReview(String userEmail, Long reviewId);
}