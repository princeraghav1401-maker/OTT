package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.ReviewRequest;
import com.streamverse.backend.dto.response.MovieRatingResponse;
import com.streamverse.backend.dto.response.ReviewResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.Review;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.ReviewRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse addOrUpdateReview(String userEmail, ReviewRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Review review = reviewRepository
                .findByUserIdAndMovieIdAndDeletedFalse(user.getId(), movie.getId())
                .orElse(
                        Review.builder()
                                .user(user)
                                .movie(movie)
                                .build()
                );

        review.setRating(request.getRating());
        review.setReview(request.getReview());

        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    @Override
    public List<ReviewResponse> getMovieReviews(Long movieId) {

        return reviewRepository
                .findByMovieIdAndDeletedFalseOrderByCreatedAtDesc(movieId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MovieRatingResponse getMovieRating(Long movieId) {

        Movie movie = movieRepository.findById(movieId)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Double averageRating = reviewRepository.getAverageRatingByMovieId(movieId);
        Long totalReviews = reviewRepository.countByMovieIdAndDeletedFalse(movieId);

        return MovieRatingResponse.builder()
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .averageRating(roundRating(averageRating))
                .totalReviews(totalReviews)
                .build();
    }

    @Override
    public void deleteReview(String userEmail, Long reviewId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .filter(r -> !Boolean.TRUE.equals(r.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Review not found"));

        boolean isOwner = review.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole() != null
                && "ADMIN".equalsIgnoreCase(user.getRole().getName());

        if (!isOwner && !isAdmin) {
            throw new RuntimeException("You are not allowed to delete this review");
        }

        review.setDeleted(true);
        reviewRepository.save(review);
    }

    private ReviewResponse mapToResponse(Review review) {

        return ReviewResponse.builder()
                .id(review.getId())
                .movieId(review.getMovie().getId())
                .movieTitle(review.getMovie().getTitle())
                .userId(review.getUser().getId())
                .userName(review.getUser().getName())
                .rating(review.getRating())
                .review(review.getReview())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    private Double roundRating(Double rating) {
        if (rating == null) {
            return 0.0;
        }

        return Math.round(rating * 10.0) / 10.0;
    }
}