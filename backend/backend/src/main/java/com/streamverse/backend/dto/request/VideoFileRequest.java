package com.streamverse.backend.dto.request;

import lombok.Data;

@Data
public class VideoFileRequest {

    private String contentType;

    private Long movieId;

    private Long episodeId;

    private String videoUrl;

    private String quality;

    private Integer durationSeconds;

    private String subtitleUrl;
}