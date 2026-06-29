package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.SeasonRequest;
import com.streamverse.backend.dto.response.SeasonResponse;
import com.streamverse.backend.entity.Season;
import com.streamverse.backend.entity.Series;
import com.streamverse.backend.repository.SeasonRepository;
import com.streamverse.backend.repository.SeriesRepository;
import com.streamverse.backend.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeasonServiceImpl implements SeasonService {

    private final SeasonRepository seasonRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public SeasonResponse createSeason(SeasonRequest request) {

        Series series = seriesRepository.findById(request.getSeriesId())
                .filter(s -> !Boolean.TRUE.equals(s.getDeleted()))
                .filter(s -> Boolean.TRUE.equals(s.getApproved()))
                .orElseThrow(() -> new RuntimeException("Series not found"));

        if (seasonRepository.existsBySeriesIdAndSeasonNumber(
                series.getId(),
                request.getSeasonNumber()
        )) {
            throw new RuntimeException("Season already exists for this series");
        }

        Season season = Season.builder()
                .series(series)
                .seasonNumber(request.getSeasonNumber())
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(request.getThumbnailUrl())
                .approved(true)
                .deleted(false)
                .build();

        Season saved = seasonRepository.save(season);

        return mapToResponse(saved);
    }

    @Override
    public List<SeasonResponse> getSeasonsBySeries(Long seriesId) {
        return seasonRepository
                .findBySeriesIdAndDeletedFalseAndApprovedTrueOrderBySeasonNumberAsc(seriesId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public SeasonResponse getSeasonById(Long id) {
        Season season = seasonRepository.findByIdAndDeletedFalseAndApprovedTrue(id)
                .orElseThrow(() -> new RuntimeException("Season not found"));

        return mapToResponse(season);
    }

    @Override
    public void deleteSeason(Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Season not found"));

        season.setDeleted(true);
        seasonRepository.save(season);
    }

    private SeasonResponse mapToResponse(Season season) {
        Series series = season.getSeries();

        return SeasonResponse.builder()
                .id(season.getId())
                .seriesId(series.getId())
                .seriesTitle(series.getTitle())
                .seasonNumber(season.getSeasonNumber())
                .title(season.getTitle())
                .description(season.getDescription())
                .thumbnailUrl(season.getThumbnailUrl())
                .createdAt(season.getCreatedAt())
                .updatedAt(season.getUpdatedAt())
                .build();
    }
}