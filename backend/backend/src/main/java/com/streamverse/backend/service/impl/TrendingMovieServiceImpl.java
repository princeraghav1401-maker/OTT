package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.TrendingMovieResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.repository.FavoriteRepository;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.ReviewRepository;
import com.streamverse.backend.repository.WatchlistRepository;
import com.streamverse.backend.service.TrendingMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrendingMovieServiceImpl implements TrendingMovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchlistRepository watchlistRepository;

    @Override
    public List<TrendingMovieResponse> getTrendingMovies() {

        return movieRepository.findAll()
                .stream()
                .filter(movie -> !Boolean.TRUE.equals(movie.getDeleted()))
                .filter(movie -> Boolean.TRUE.equals(movie.getApproved()))
                .map(this::mapToTrendingResponse)
                .sorted(Comparator.comparing(
                        TrendingMovieResponse::getTrendingScore
                ).reversed())
                .toList();
    }

    private TrendingMovieResponse mapToTrendingResponse(Movie movie) {

        Long reviewCount = reviewRepository.countByMovieIdAndDeletedFalse(movie.getId());
        Long favoriteCount = favoriteRepository.countByMovieId(movie.getId());
        Long watchlistCount = watchlistRepository.countByMovieId(movie.getId());

        Double averageRating = reviewRepository.getAverageRatingByMovieId(movie.getId());

        if (averageRating == null) {
            averageRating = 0.0;
        }

        double trendingScore =
                reviewCount +
                        favoriteCount +
                        watchlistCount +
                        (averageRating * 10);

        return TrendingMovieResponse.builder()
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
                .reviewCount(reviewCount)
                .favoriteCount(favoriteCount)
                .watchlistCount(watchlistCount)
                .averageRating(averageRating)
                .trendingScore(trendingScore)
                .build();
    }
}