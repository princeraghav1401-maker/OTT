package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoPlayResponse {

    private Long movieId;
    private String title;
    private Boolean premium;

    private String videoUrl;
    private String quality;
    private Integer durationSeconds;
    private String subtitlesUrl;
}