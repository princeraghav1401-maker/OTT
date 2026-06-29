package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.UserResponse;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateCurrentUserProfile(
            String email,
            String name,
            String phone,
            MultipartFile profileImage
    ) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (name != null && !name.trim().isEmpty()) {
                user.setName(name.trim());
            }

            if (phone != null) {
                user.setPhone(phone.trim());
            }

            if (profileImage != null && !profileImage.isEmpty()) {
                String contentType = profileImage.getContentType();

                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new RuntimeException("Only image files are allowed");
                }

                String originalName = profileImage.getOriginalFilename();
                String extension = "";

                if (originalName != null && originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }

                String fileName = UUID.randomUUID() + extension;

                Path uploadDir = Paths.get("uploads/profile-images");
                Files.createDirectories(uploadDir);

                Path filePath = uploadDir.resolve(fileName);
                Files.write(filePath, profileImage.getBytes());

                user.setProfileImage("/uploads/profile-images/" + fileName);
            }

            User savedUser = userRepository.save(user);

            return mapToResponse(savedUser);

        } catch (Exception e) {
            throw new RuntimeException("Unable to update profile: " + e.getMessage());
        }
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .profileImage(user.getProfileImage())
                .role(user.getRole().getName())
                .build();
    }
}