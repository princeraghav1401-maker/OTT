package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.WatchProgressRequest;
import com.streamverse.backend.dto.response.ContinueWatchingResponse;
import com.streamverse.backend.service.WatchHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
@RequiredArgsConstructor
public class WatchHistoryController {

    private final WatchHistoryService watchHistoryService;

    @PostMapping("/progress")
    public ResponseEntity<ContinueWatchingResponse> saveProgress(
            @Valid @RequestBody WatchProgressRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                watchHistoryService.saveProgress(email, request)
        );
    }

    @GetMapping("/continue")
    public ResponseEntity<List<ContinueWatchingResponse>> getContinueWatching(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(
                watchHistoryService.getContinueWatching(email)
        );
    }
}