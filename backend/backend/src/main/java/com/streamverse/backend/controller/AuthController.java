package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.LoginRequest;
import com.streamverse.backend.dto.request.RegisterRequest;
import com.streamverse.backend.dto.request.ResendOtpRequest;
import com.streamverse.backend.dto.request.VerifyEmailRequest;
import com.streamverse.backend.dto.response.AuthResponse;
import com.streamverse.backend.dto.response.MessageResponse;
import com.streamverse.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.streamverse.backend.dto.request.GoogleLoginRequest;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request
    ) {
        return ResponseEntity.ok(authService.verifyEmail(request));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<MessageResponse> resendOtp(
            @Valid @RequestBody ResendOtpRequest request
    ) {
        return ResponseEntity.ok(authService.resendOtp(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @RequestBody GoogleLoginRequest request
    ) {
        return ResponseEntity.ok(authService.googleLogin(request));
    }
}