package com.streamverse.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MovieRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String slug;

    private String description;
    private String cast;
    private String director;

    private Integer releaseYear;
    private Integer durationMinutes;
    private String ageRating;

    private String thumbnailUrl;
    private String bannerUrl;
    private String trailerUrl;

    private Boolean premium;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long languageId;

    private List<Long> genreIds;
}