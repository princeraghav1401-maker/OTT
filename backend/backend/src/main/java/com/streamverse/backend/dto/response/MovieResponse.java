package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovieResponse {

    private Long id;
    private String title;
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
    private Long views;

    private String category;
    private String language;
    private List<String> genres;
}