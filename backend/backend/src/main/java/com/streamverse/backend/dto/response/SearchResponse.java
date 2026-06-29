package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SearchResponse {

    private String query;

    private List<MovieResponse> movies;

    private List<SeriesResponse> series;
}