package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.FavoriteResponse;
import com.streamverse.backend.entity.Favorite;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.FavoriteRepository;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public FavoriteResponse addToFavorites(String userEmail, Long movieId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Favorite favorite = favoriteRepository
                .findByUserIdAndMovieId(user.getId(), movie.getId())
                .orElseGet(() -> favoriteRepository.save(
                        Favorite.builder()
                                .user(user)
                                .movie(movie)
                                .build()
                ));

        return mapToResponse(favorite);
    }

    @Override
    @Transactional
    public void removeFromFavorites(String userEmail, Long movieId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = favoriteRepository.existsByUserIdAndMovieId(user.getId(), movieId);

        if (!exists) {
            throw new RuntimeException("Movie not found in favorites");
        }

        favoriteRepository.deleteByUserIdAndMovieId(user.getId(), movieId);
    }

    @Override
    public List<FavoriteResponse> getMyFavorites(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public boolean isFavorite(String userEmail, Long movieId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return favoriteRepository.existsByUserIdAndMovieId(user.getId(), movieId);
    }

    private FavoriteResponse mapToResponse(Favorite favorite) {

        Movie movie = favorite.getMovie();

        return FavoriteResponse.builder()
                .id(favorite.getId())
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
                .addedAt(favorite.getCreatedAt())
                .build();
    }
}