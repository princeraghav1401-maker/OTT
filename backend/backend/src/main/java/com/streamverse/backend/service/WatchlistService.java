package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.WatchlistResponse;

import java.util.List;

public interface WatchlistService {

    WatchlistResponse addToWatchlist(String userEmail, Long movieId);

    void removeFromWatchlist(String userEmail, Long movieId);

    List<WatchlistResponse> getMyWatchlist(String userEmail);
}