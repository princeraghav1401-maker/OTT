package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.MovieResponse;
import com.streamverse.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(movieService.searchMovies(keyword));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MovieResponse>> filterMovies(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long languageId,
            @RequestParam(required = false) Integer releaseYear
    ) {
        return ResponseEntity.ok(
                movieService.filterMovies(categoryId, languageId, releaseYear)
        );
    }
}