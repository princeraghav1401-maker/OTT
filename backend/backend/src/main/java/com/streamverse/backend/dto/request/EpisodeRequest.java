package com.streamverse.backend.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodeRequest {

    private Long seriesId;

    private Integer seasonNumber;

    private Integer episodeNumber;

    private String title;

    private String slug;

    private String description;

    private Integer durationMinutes;

    private String thumbnailUrl;

    private LocalDateTime releaseDate;
}