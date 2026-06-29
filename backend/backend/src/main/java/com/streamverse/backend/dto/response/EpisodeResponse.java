package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EpisodeResponse {

    private Long id;

    private Long seriesId;

    private String seriesTitle;

    private Integer seasonNumber;

    private Integer episodeNumber;

    private String title;

    private String slug;

    private String description;

    private Integer durationMinutes;

    private String thumbnailUrl;

    private LocalDateTime releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}