package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.UserStatsResponse;
import com.streamverse.backend.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/me/stats")
@RequiredArgsConstructor
public class UserStatsController {

    private final UserStatsService userStatsService;

    @GetMapping
    public ResponseEntity<UserStatsResponse> getMyStats(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                userStatsService.getMyStats(authentication.getName())
        );
    }
}