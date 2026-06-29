package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.UpdateProfileRequest;
import com.streamverse.backend.dto.response.UserProfileResponse;
import com.streamverse.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                profileService.getProfile(authentication.getName())
        );
    }

    @PutMapping
    public ResponseEntity<UserProfileResponse> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(
                profileService.updateProfile(authentication.getName(), request)
        );
    }
}