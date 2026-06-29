package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.WatchlistResponse;
import com.streamverse.backend.service.WatchlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @PostMapping("/add/{movieId}")
    public ResponseEntity<WatchlistResponse> addToWatchlist(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                watchlistService.addToWatchlist(email, movieId)
        );
    }

    @DeleteMapping("/remove/{movieId}")
    public ResponseEntity<String> removeFromWatchlist(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        watchlistService.removeFromWatchlist(email, movieId);

        return ResponseEntity.ok("Movie removed from watchlist successfully");
    }

    @GetMapping("/my-list")
    public ResponseEntity<List<WatchlistResponse>> getMyWatchlist(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                watchlistService.getMyWatchlist(email)
        );
    }
}