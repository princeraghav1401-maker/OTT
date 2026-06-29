package com.streamverse.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WatchProgressRequest {

    @NotNull
    private Long movieId;

    @NotNull
    @Min(0)
    private Integer watchedSeconds;

    private Boolean completed;
}