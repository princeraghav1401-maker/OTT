package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.RecentMovieResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.service.RecentMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecentMovieServiceImpl implements RecentMovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<RecentMovieResponse> getRecentMovies() {
        return movieRepository
                .findTop20ByDeletedFalseAndApprovedTrueOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RecentMovieResponse mapToResponse(Movie movie) {
        return RecentMovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .slug(movie.getSlug())
                .thumbnailUrl(movie.getThumbnailUrl())
                .bannerUrl(movie.getBannerUrl())
                .releaseYear(movie.getReleaseYear())
                .ageRating(movie.getAgeRating())
                .premium(movie.getPremium())
                .category(movie.getCategory() != null ? movie.getCategory().getName() : null)
                .language(movie.getLanguage() != null ? movie.getLanguage().getName() : null)
                .build();
    }
}