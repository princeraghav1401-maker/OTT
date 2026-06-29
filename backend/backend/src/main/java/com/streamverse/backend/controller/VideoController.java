package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.VideoPlayResponse;
import com.streamverse.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<VideoPlayResponse> getMovieVideo(
            @PathVariable Long movieId,
            Authentication authentication
    ) {
        String userEmail = authentication != null ? authentication.getName() : null;

        return ResponseEntity.ok(
                videoService.getMovieVideo(movieId, userEmail)
        );
    }
}