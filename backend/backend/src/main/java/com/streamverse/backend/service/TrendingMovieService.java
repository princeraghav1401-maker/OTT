package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.TrendingMovieResponse;

import java.util.List;

public interface TrendingMovieService {

    List<TrendingMovieResponse> getTrendingMovies();
}