package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    boolean existsBySlug(String slug);

    Optional<Series> findBySlugAndDeletedFalseAndApprovedTrue(String slug);

    List<Series> findByDeletedFalseAndApprovedTrue();

    List<Series> findByTitleContainingIgnoreCaseAndDeletedFalseAndApprovedTrue(String keyword);

    List<Series> findByCategoryIdAndDeletedFalseAndApprovedTrue(Long categoryId);

    List<Series> findByLanguageIdAndDeletedFalseAndApprovedTrue(Long languageId);

    List<Series> findByReleaseYearAndDeletedFalseAndApprovedTrue(Integer releaseYear);


    @Query("""
        SELECT DISTINCT s FROM Series s
        LEFT JOIN s.category c
        LEFT JOIN s.language l
        LEFT JOIN s.genres g
        WHERE s.deleted = false
          AND s.approved = true
          AND (
                LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.cast) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(s.director) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
             OR LOWER(g.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
        ORDER BY s.id DESC
        """)
    List<Series> searchSeries(@Param("keyword") String keyword);
}