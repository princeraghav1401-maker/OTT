package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.MovieRequest;
import com.streamverse.backend.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {

    List<MovieResponse> getAllMovies();

    MovieResponse getMovieById(Long id);

    List<MovieResponse> searchMovies(String keyword);

    List<MovieResponse> filterMovies(Long categoryId, Long languageId, Integer releaseYear);

    MovieResponse createMovie(MovieRequest request);

    MovieResponse updateMovie(Long id, MovieRequest request);

    void deleteMovie(Long id);
}