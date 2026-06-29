package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    FavoriteResponse addToFavorites(String userEmail, Long movieId);

    void removeFromFavorites(String userEmail, Long movieId);

    List<FavoriteResponse> getMyFavorites(String userEmail);

    boolean isFavorite(String userEmail, Long movieId);
}