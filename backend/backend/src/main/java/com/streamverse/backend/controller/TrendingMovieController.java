package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.TrendingMovieResponse;
import com.streamverse.backend.service.TrendingMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trending")
@RequiredArgsConstructor
public class TrendingMovieController {

    private final TrendingMovieService trendingMovieService;

    @GetMapping("/movies")
    public ResponseEntity<List<TrendingMovieResponse>> getTrendingMovies() {
        return ResponseEntity.ok(
                trendingMovieService.getTrendingMovies()
        );
    }
}