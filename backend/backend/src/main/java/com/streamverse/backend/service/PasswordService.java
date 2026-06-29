package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.ForgotPasswordRequest;
import com.streamverse.backend.dto.request.ResetPasswordRequest;

public interface PasswordService {

    String forgotPassword(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request);
}