package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndMovieId(Long userId, Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    List<Favorite> findByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByUserIdAndMovieId(Long userId, Long movieId);

    Long countByUserId(Long userId);


    Long countByMovieId(Long movieId);
}