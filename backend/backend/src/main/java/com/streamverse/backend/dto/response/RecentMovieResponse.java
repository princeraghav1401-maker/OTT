package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentMovieResponse {

    private Long id;
    private String title;
    private String slug;
    private String thumbnailUrl;
    private String bannerUrl;
    private Integer releaseYear;
    private String ageRating;
    private Boolean premium;
    private String category;
    private String language;
}