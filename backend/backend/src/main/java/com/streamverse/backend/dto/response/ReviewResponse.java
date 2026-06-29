package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {

    private Long id;
    private Long movieId;
    private String movieTitle;

    private Long userId;
    private String userName;

    private Integer rating;
    private String review;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}