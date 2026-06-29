package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.ForgotPasswordRequest;
import com.streamverse.backend.dto.request.ResetPasswordRequest;
import com.streamverse.backend.entity.PasswordResetToken;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.PasswordResetTokenRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.EmailService;
import com.streamverse.backend.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);

        emailService.sendForgotPasswordEmail(
                user.getEmail(),
                token
        );

        return "Password reset token generated. Check console/email.";
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {

        PasswordResetToken resetToken = tokenRepository
                .findByTokenAndUsedFalse(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or used reset token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        resetToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(resetToken);

        return "Password reset successfully";
    }
}