package com.streamverse.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeasonRequest {

    @NotNull
    private Long seriesId;

    @NotNull
    @Min(1)
    private Integer seasonNumber;

    @NotBlank
    private String title;

    private String description;

    private String thumbnailUrl;
}