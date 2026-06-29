package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.EpisodeRequest;
import com.streamverse.backend.dto.response.EpisodeResponse;
import com.streamverse.backend.entity.Episode;
import com.streamverse.backend.entity.Series;
import com.streamverse.backend.repository.EpisodeRepository;
import com.streamverse.backend.repository.SeriesRepository;
import com.streamverse.backend.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public EpisodeResponse createEpisode(EpisodeRequest request) {

        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new RuntimeException("Series not found"));

        Episode episode = Episode.builder()
                .series(series)
                .seasonNumber(request.getSeasonNumber())
                .episodeNumber(request.getEpisodeNumber())
                .title(request.getTitle())
                .slug(request.getSlug())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .thumbnailUrl(request.getThumbnailUrl())
                .releaseDate(request.getReleaseDate())
                .build();

        episode = episodeRepository.save(episode);

        return mapToResponse(episode);
    }

    @Override
    public EpisodeResponse getEpisodeById(Long id) {

        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        return mapToResponse(episode);
    }

    @Override
    public List<EpisodeResponse> getEpisodesBySeries(Long seriesId) {

        return episodeRepository.findBySeriesId(seriesId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteEpisode(Long id) {

        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        episode.setDeleted(true);

        episodeRepository.save(episode);
    }

    private EpisodeResponse mapToResponse(Episode episode) {

        return EpisodeResponse.builder()
                .id(episode.getId())
                .seriesId(episode.getSeries().getId())
                .seriesTitle(episode.getSeries().getTitle())
                .seasonNumber(episode.getSeasonNumber())
                .episodeNumber(episode.getEpisodeNumber())
                .title(episode.getTitle())
                .slug(episode.getSlug())
                .description(episode.getDescription())
                .durationMinutes(episode.getDurationMinutes())
                .thumbnailUrl(episode.getThumbnailUrl())
                .releaseDate(episode.getReleaseDate())
                .createdAt(episode.getCreatedAt())
                .updatedAt(episode.getUpdatedAt())
                .build();
    }
}