package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.UserResponse;
import com.streamverse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(
                userService.getCurrentUserProfile(authentication.getName())
        );
    }

    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateProfile(
            Authentication authentication,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestPart(required = false) MultipartFile profileImage
    ) {
        return ResponseEntity.ok(
                userService.updateCurrentUserProfile(
                        authentication.getName(),
                        name,
                        phone,
                        profileImage
                )
        );
    }
}