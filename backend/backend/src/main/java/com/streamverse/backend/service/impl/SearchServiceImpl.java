package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.MovieResponse;
import com.streamverse.backend.dto.response.SearchResponse;
import com.streamverse.backend.dto.response.SeriesResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.Series;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.SeriesRepository;
import com.streamverse.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public SearchResponse search(String query) {

        String keyword = query == null ? "" : query.trim();

        if (keyword.isBlank()) {
            return SearchResponse.builder()
                    .query(keyword)
                    .movies(List.of())
                    .series(List.of())
                    .build();
        }

        List<MovieResponse> movies = movieRepository
                .searchMovies(keyword)
                .stream()
                .map(this::mapMovie)
                .toList();

        List<SeriesResponse> series = seriesRepository
                .searchSeries(keyword)
                .stream()
                .map(this::mapSeries)
                .toList();

        return SearchResponse.builder()
                .query(keyword)
                .movies(movies)
                .series(series)
                .build();
    }

    private MovieResponse mapMovie(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .slug(movie.getSlug())
                .description(movie.getDescription())
                .cast(movie.getCast())
                .director(movie.getDirector())
                .releaseYear(movie.getReleaseYear())
                .durationMinutes(movie.getDurationMinutes())
                .ageRating(movie.getAgeRating())
                .thumbnailUrl(movie.getThumbnailUrl())
                .bannerUrl(movie.getBannerUrl())
                .trailerUrl(movie.getTrailerUrl())
                .premium(movie.getPremium())
                .views(movie.getViews())
                .category(movie.getCategory() != null ? movie.getCategory().getName() : null)
                .language(movie.getLanguage() != null ? movie.getLanguage().getName() : null)
                .genres(
                        movie.getGenres() == null
                                ? List.of()
                                : movie.getGenres()
                                .stream()
                                .map(genre -> genre.getName())
                                .toList()
                )
                .build();
    }

    private SeriesResponse mapSeries(Series series) {
        return SeriesResponse.builder()
                .id(series.getId())
                .title(series.getTitle())
                .slug(series.getSlug())
                .description(series.getDescription())
                .cast(series.getCast())
                .director(series.getDirector())
                .releaseYear(series.getReleaseYear())
                .ageRating(series.getAgeRating())
                .thumbnailUrl(series.getThumbnailUrl())
                .bannerUrl(series.getBannerUrl())
                .trailerUrl(series.getTrailerUrl())
                .premium(series.getPremium())
                .category(series.getCategory() != null ? series.getCategory().getName() : null)
                .language(series.getLanguage() != null ? series.getLanguage().getName() : null)
                .genres(
                        series.getGenres() == null
                                ? List.of()
                                : series.getGenres()
                                .stream()
                                .map(genre -> genre.getName())
                                .toList()
                )
                .build();
    }
}