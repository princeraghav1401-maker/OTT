package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SeasonResponse {

    private Long id;

    private Long seriesId;
    private String seriesTitle;

    private Integer seasonNumber;
    private String title;
    private String description;
    private String thumbnailUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}