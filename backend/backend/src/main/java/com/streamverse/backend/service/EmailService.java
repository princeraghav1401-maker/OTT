package com.streamverse.backend.service;

public interface EmailService {

    void sendForgotPasswordEmail(String toEmail, String resetToken);

    void sendEmailVerificationOtp(String toEmail, String otp);
}