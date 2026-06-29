package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.ContinueWatchingResponse;
import com.streamverse.backend.service.ContinueWatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/continue-watching")
@RequiredArgsConstructor
public class ContinueWatchingController {

    private final ContinueWatchingService continueWatchingService;

    @GetMapping
    public ResponseEntity<List<ContinueWatchingResponse>> getContinueWatching(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                continueWatchingService.getContinueWatching(authentication.getName())
        );
    }
}