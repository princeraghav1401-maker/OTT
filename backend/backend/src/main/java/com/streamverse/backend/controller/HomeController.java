package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.HomeResponse;
import com.streamverse.backend.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseEntity<HomeResponse> getHome(Authentication authentication) {

        String email = null;

        if (authentication != null && authentication.isAuthenticated()) {
            email = authentication.getName();
        }

        return ResponseEntity.ok(homeService.getHome(email));
    }
}