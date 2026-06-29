package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Movie> findByDeletedFalseAndApprovedTrue();

    List<Movie> findByTitleContainingIgnoreCaseAndDeletedFalseAndApprovedTrue(String title);

    List<Movie> findByCategoryIdAndDeletedFalseAndApprovedTrue(Long categoryId);

    List<Movie> findByLanguageIdAndDeletedFalseAndApprovedTrue(Long languageId);

    List<Movie> findByReleaseYearAndDeletedFalseAndApprovedTrue(Integer releaseYear);

    @Query("""
        SELECT DISTINCT m FROM Movie m
        LEFT JOIN m.category c
        LEFT JOIN m.language l
        LEFT JOIN m.genres g
        WHERE m.deleted = false
          AND m.approved = true
          AND (
                LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(m.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(m.cast) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(m.director) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY m.views DESC
        """)
    List<Movie> searchMovies(@Param("keyword") String keyword);

    List<Movie> findTop20ByDeletedFalseAndApprovedTrueOrderByCreatedAtDesc();
}