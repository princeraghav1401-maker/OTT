package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.LoginRequest;
import com.streamverse.backend.dto.request.RegisterRequest;
import com.streamverse.backend.dto.request.ResendOtpRequest;
import com.streamverse.backend.dto.request.VerifyEmailRequest;
import com.streamverse.backend.dto.response.AuthResponse;
import com.streamverse.backend.dto.response.MessageResponse;
import com.streamverse.backend.entity.Role;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.RoleRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.security.jwt.JwtService;
import com.streamverse.backend.service.AuthService;
import com.streamverse.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.streamverse.backend.dto.request.GoogleLoginRequest;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public MessageResponse register(RegisterRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        if (!email.endsWith("@gmail.com")) {
            throw new RuntimeException("Only Gmail addresses are allowed");
        }

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("USER role not found"));

        String otp = generateOtp();

        User user = User.builder()
                .name(request.getName())
                .email(email)
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .active(true)
                .deleted(false)
                .emailVerified(false)
                .emailOtp(otp)
                .otpExpiry(LocalDateTime.now().plusMinutes(10))
                .build();

        userRepository.save(user);

        emailService.sendEmailVerificationOtp(email, otp);

        return new MessageResponse("Account created. OTP sent to your Gmail.");
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.FALSE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Please verify your email before login");
        }

        if (Boolean.FALSE.equals(user.getActive()) || Boolean.TRUE.equals(user.getDeleted())) {
            throw new RuntimeException("Account is not active");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName());

        return new AuthResponse(token, user.getEmail(), user.getRole().getName());
    }

    @Override
    public MessageResponse verifyEmail(VerifyEmailRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return new MessageResponse("Email already verified");
        }

        if (user.getEmailOtp() == null || user.getOtpExpiry() == null) {
            throw new RuntimeException("OTP not found. Please resend OTP.");
        }

        if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
            throw new RuntimeException("OTP expired. Please resend OTP.");
        }

        if (!user.getEmailOtp().equals(request.getOtp().trim())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setEmailVerified(true);
        user.setEmailOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        return new MessageResponse("Email verified successfully. You can login now.");
    }

    @Override
    public MessageResponse resendOtp(ResendOtpRequest request) {

        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getEmailVerified())) {
            return new MessageResponse("Email already verified");
        }

        String otp = generateOtp();

        user.setEmailOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendEmailVerificationOtp(email, otp);

        return new MessageResponse("New OTP sent to your Gmail.");
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Value("${google.client-id}")
    private String googleClientId;


    @Override
    public AuthResponse googleLogin(GoogleLoginRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getCredential());

            if (idToken == null) {
                throw new RuntimeException("Invalid Google token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");

            if (email == null || !email.endsWith("@gmail.com")) {
                throw new RuntimeException("Only Gmail accounts are allowed");
            }

            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("USER role not found"));

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .name(name != null ? name : "Google User")
                                .email(email)
                                .password(passwordEncoder.encode("GOOGLE_LOGIN"))
                                .role(userRole)
                                .active(true)
                                .deleted(false)
                                .emailVerified(true)
                                .emailOtp(null)
                                .otpExpiry(null)
                                .build();

                        return userRepository.save(newUser);
                    });

            user.setEmailVerified(true);
            userRepository.save(user);

            String token = jwtService.generateToken(user.getEmail(), user.getRole().getName());

            return new AuthResponse(token, user.getEmail(), user.getRole().getName());

        } catch (Exception e) {
            throw new RuntimeException("Google login failed: " + e.getMessage());
        }
    }
}