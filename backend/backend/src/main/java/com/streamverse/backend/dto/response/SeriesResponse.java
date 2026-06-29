package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SeriesResponse {

    private Long id;
    private String title;
    private String slug;
    private String description;
    private String cast;
    private String director;

    private Integer releaseYear;
    private String ageRating;

    private String thumbnailUrl;
    private String bannerUrl;
    private String trailerUrl;

    private Boolean premium;

    private String category;
    private String language;

    private List<String> genres;
}