package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.ForgotPasswordRequest;
import com.streamverse.backend.dto.request.ResetPasswordRequest;
import com.streamverse.backend.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(
                passwordService.forgotPassword(request)
        );
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(
                passwordService.resetPassword(request)
        );
    }
}