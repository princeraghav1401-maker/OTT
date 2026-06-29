package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.*;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.Series;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.SeriesRepository;
import com.streamverse.backend.service.HomeService;
import com.streamverse.backend.service.WatchHistoryService;
import com.streamverse.backend.service.WatchlistService;
import com.streamverse.backend.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final WatchHistoryService watchHistoryService;
    private final WatchlistService watchlistService;
    private final YouTubeService youTubeService;

    private final Random random = new Random();

    private final List<String> trailerQueries = List.of(
            "Latest Bollywood Trailer",
            "Latest Hollywood Trailer",
            "Latest South Movie Trailer",
            "Latest Netflix Trailer",
            "Latest Anime Trailer",
            "Latest Marvel Trailer",
            "Latest DC Trailer"
    );

    private final List<String> musicQueries = List.of(
            "Latest Hindi Songs",
            "Trending English Songs",
            "Latest Punjabi Songs",
            "Trending Tamil Songs",
            "Latest Telugu Songs"
    );

    @Override
    public HomeResponse getHome(String userEmail) {

        List<Movie> movies = movieRepository.findByDeletedFalseAndApprovedTrue();

        List<MovieResponse> latestMovies = movies.stream()
                .sorted(Comparator.comparing(Movie::getId).reversed())
                .limit(10)
                .map(this::mapMovie)
                .toList();

        List<MovieResponse> trendingMovies = movies.stream()
                .sorted(Comparator.comparing(
                        m -> m.getViews() == null ? 0L : m.getViews(),
                        Comparator.reverseOrder()
                ))
                .limit(10)
                .map(this::mapMovie)
                .toList();

        List<MovieResponse> premiumMovies = movies.stream()
                .filter(m -> Boolean.TRUE.equals(m.getPremium()))
                .limit(10)
                .map(this::mapMovie)
                .toList();

        List<SeriesResponse> latestSeries = seriesRepository
                .findByDeletedFalseAndApprovedTrue()
                .stream()
                .sorted(Comparator.comparing(Series::getId).reversed())
                .limit(10)
                .map(this::mapSeries)
                .toList();

        List<ContinueWatchingResponse> continueWatching = List.of();
        List<WatchlistResponse> myWatchlist = List.of();

        if (userEmail != null && !userEmail.isBlank()) {
            try {
                continueWatching = watchHistoryService.getContinueWatching(userEmail);
            } catch (Exception ignored) {
            }

            try {
                myWatchlist = watchlistService.getMyWatchlist(userEmail);
            } catch (Exception ignored) {
            }
        }

        List<YoutubeVideoResponse> latestTrailers = safeYoutubeSearch(
                trailerQueries.get(random.nextInt(trailerQueries.size()))
        );

        List<YoutubeVideoResponse> latestSongs = safeYoutubeSearch(
                musicQueries.get(random.nextInt(musicQueries.size()))
        );

        List<YoutubeVideoResponse> trendingYoutube = safeYoutubeSearch(
                "Trending Movie Trailers"
        );

        return HomeResponse.builder()
                .trendingMovies(trendingMovies)
                .latestMovies(latestMovies)
                .premiumMovies(premiumMovies)
                .latestSeries(latestSeries)
                .continueWatching(continueWatching)
                .myWatchlist(myWatchlist)
                .latestTrailers(latestTrailers)
                .latestSongs(latestSongs)
                .trendingYoutube(trendingYoutube)
                .build();
    }

    private List<YoutubeVideoResponse> safeYoutubeSearch(String query) {
        try {
            return youTubeService.searchVideos(query);
        } catch (Exception e) {
            return List.of();
        }
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