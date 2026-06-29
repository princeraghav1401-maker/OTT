package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {

    boolean existsBySeriesIdAndSeasonNumber(Long seriesId, Integer seasonNumber);

    Optional<Season> findByIdAndDeletedFalseAndApprovedTrue(Long id);

    List<Season> findBySeriesIdAndDeletedFalseAndApprovedTrueOrderBySeasonNumberAsc(Long seriesId);
}