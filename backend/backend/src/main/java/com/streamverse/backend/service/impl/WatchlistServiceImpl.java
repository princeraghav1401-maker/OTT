package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.WatchlistResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.entity.Watchlist;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.repository.WatchlistRepository;
import com.streamverse.backend.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public WatchlistResponse addToWatchlist(String userEmail, Long movieId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Watchlist watchlist = watchlistRepository
                .findByUserIdAndMovieId(user.getId(), movie.getId())
                .orElseGet(() -> watchlistRepository.save(
                        Watchlist.builder()
                                .user(user)
                                .movie(movie)
                                .build()
                ));

        return mapToResponse(watchlist);
    }

    @Override
    @Transactional
    public void removeFromWatchlist(String userEmail, Long movieId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = watchlistRepository.existsByUserIdAndMovieId(
                user.getId(),
                movieId
        );

        if (!exists) {
            throw new RuntimeException("Movie not found in watchlist");
        }

        watchlistRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
    }

    @Override
    public List<WatchlistResponse> getMyWatchlist(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return watchlistRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private WatchlistResponse mapToResponse(Watchlist watchlist) {

        Movie movie = watchlist.getMovie();

        return WatchlistResponse.builder()
                .id(watchlist.getId())
                .movieId(movie.getId())
                .title(movie.getTitle())
                .slug(movie.getSlug())
                .thumbnailUrl(movie.getThumbnailUrl())
                .bannerUrl(movie.getBannerUrl())
                .releaseYear(movie.getReleaseYear())
                .ageRating(movie.getAgeRating())
                .premium(movie.getPremium())
                .category(movie.getCategory() != null ? movie.getCategory().getName() : null)
                .language(movie.getLanguage() != null ? movie.getLanguage().getName() : null)
                .addedAt(watchlist.getCreatedAt())
                .build();
    }
}