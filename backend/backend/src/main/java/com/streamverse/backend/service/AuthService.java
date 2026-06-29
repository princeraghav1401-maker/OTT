package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.LoginRequest;
import com.streamverse.backend.dto.request.RegisterRequest;
import com.streamverse.backend.dto.request.ResendOtpRequest;
import com.streamverse.backend.dto.request.VerifyEmailRequest;
import com.streamverse.backend.dto.response.AuthResponse;
import com.streamverse.backend.dto.response.MessageResponse;
import com.streamverse.backend.dto.request.GoogleLoginRequest;

public interface AuthService {

    MessageResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    MessageResponse verifyEmail(VerifyEmailRequest request);

    MessageResponse resendOtp(ResendOtpRequest request);

    AuthResponse googleLogin(GoogleLoginRequest request);
}