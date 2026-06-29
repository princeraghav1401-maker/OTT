package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.RecentMovieResponse;
import com.streamverse.backend.service.RecentMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recent")
@RequiredArgsConstructor
public class RecentMovieController {

    private final RecentMovieService recentMovieService;

    @GetMapping("/movies")
    public ResponseEntity<List<RecentMovieResponse>> getRecentMovies() {
        return ResponseEntity.ok(recentMovieService.getRecentMovies());
    }
}