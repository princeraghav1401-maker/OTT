package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WatchlistResponse {

    private Long id;

    private Long movieId;
    private String title;
    private String slug;
    private String thumbnailUrl;
    private String bannerUrl;

    private Integer releaseYear;
    private String ageRating;
    private Boolean premium;

    private String category;
    private String language;

    private LocalDateTime addedAt;
}