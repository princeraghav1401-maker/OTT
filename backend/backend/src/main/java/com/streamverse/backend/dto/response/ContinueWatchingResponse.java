package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContinueWatchingResponse {

    private Long movieId;

    private String title;
    private String slug;
    private String thumbnail;

    private Integer watchedSeconds;
    private Integer durationSeconds;

    private Double progressPercentage;
}