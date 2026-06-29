package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.MovieResponse;
import com.streamverse.backend.dto.response.RecommendationResponse;
import com.streamverse.backend.entity.Genre;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.entity.WatchHistory;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.repository.WatchHistoryRepository;
import com.streamverse.backend.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final WatchHistoryRepository watchHistoryRepository;

    @Override
    public RecommendationResponse getRecommendations(String userEmail) {

        if (userEmail == null || userEmail.isBlank()) {
            return trendingFallback("Trending movies for guest user");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElse(null);

        if (user == null) {
            return trendingFallback("Trending movies");
        }

        List<WatchHistory> histories =
                watchHistoryRepository.findByUserIdOrderByLastWatchedAtDesc(user.getId());

        if (histories == null || histories.isEmpty()) {
            return trendingFallback("Trending movies because no watch history found");
        }

        Set<Long> watchedMovieIds = new HashSet<>();
        Set<Long> genreIds = new HashSet<>();

        for (WatchHistory history : histories) {
            if (history.getMovie() == null) {
                continue;
            }

            Movie watchedMovie = history.getMovie();

            watchedMovieIds.add(watchedMovie.getId());

            if (watchedMovie.getGenres() != null) {
                for (Genre genre : watchedMovie.getGenres()) {
                    genreIds.add(genre.getId());
                }
            }
        }

        List<Movie> candidates = movieRepository.findByDeletedFalseAndApprovedTrue();

        List<MovieResponse> recommended = candidates.stream()
                .filter(movie -> !watchedMovieIds.contains(movie.getId()))
                .filter(movie -> movie.getGenres() != null)
                .filter(movie -> movie.getGenres()
                        .stream()
                        .anyMatch(genre -> genreIds.contains(genre.getId()))
                )
                .sorted(Comparator.comparing(
                        m -> m.getViews() == null ? 0L : m.getViews(),
                        Comparator.reverseOrder()
                ))
                .limit(10)
                .map(this::mapMovie)
                .toList();

        if (recommended.isEmpty()) {
            return trendingFallback("Trending movies because no similar movies found");
        }

        return RecommendationResponse.builder()
                .reason("Recommended because of your watch history")
                .recommendedMovies(recommended)
                .build();
    }

    private RecommendationResponse trendingFallback(String reason) {

        List<MovieResponse> movies = movieRepository.findByDeletedFalseAndApprovedTrue()
                .stream()
                .sorted(Comparator.comparing(
                        m -> m.getViews() == null ? 0L : m.getViews(),
                        Comparator.reverseOrder()
                ))
                .limit(10)
                .map(this::mapMovie)
                .toList();

        return RecommendationResponse.builder()
                .reason(reason)
                .recommendedMovies(movies)
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
                                .map(Genre::getName)
                                .toList()
                )
                .build();
    }
}