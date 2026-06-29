package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.RecentMovieResponse;

import java.util.List;

public interface RecentMovieService {

    List<RecentMovieResponse> getRecentMovies();
}