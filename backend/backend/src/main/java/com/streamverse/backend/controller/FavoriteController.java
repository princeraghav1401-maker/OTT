package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.FavoriteResponse;
import com.streamverse.backend.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{movieId}")
    public ResponseEntity<FavoriteResponse> addToFavorites(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                favoriteService.addToFavorites(authentication.getName(), movieId)
        );
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> removeFromFavorites(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        favoriteService.removeFromFavorites(authentication.getName(), movieId);
        return ResponseEntity.ok("Movie removed from favorites successfully");
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getMyFavorites(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                favoriteService.getMyFavorites(authentication.getName())
        );
    }

    @GetMapping("/check/{movieId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        boolean favorite = favoriteService.isFavorite(authentication.getName(), movieId);
        return ResponseEntity.ok(Map.of("favorite", favorite));
    }
}