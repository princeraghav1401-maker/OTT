package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndMovieIdAndDeletedFalse(Long userId, Long movieId);

    List<Review> findByMovieIdAndDeletedFalseOrderByCreatedAtDesc(Long movieId);

    Long countByMovieIdAndDeletedFalse(Long movieId);

    @Query("""
            SELECT COALESCE(AVG(r.rating), 0)
            FROM Review r
            WHERE r.movie.id = :movieId
            AND r.deleted = false
            """)
    Double getAverageRatingByMovieId(Long movieId);

    Long countByUserIdAndDeletedFalse(Long userId);
}