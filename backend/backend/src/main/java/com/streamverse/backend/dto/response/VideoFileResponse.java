package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoFileResponse {

    private Long id;
    private String contentType;
    private Long movieId;
    private String fileName;
    private String filePath;
    private String fileUrl;
    private String quality;
    private Integer durationSeconds;
    private Long fileSize;
    private String subtitlesUrl;
}