package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserResponse getCurrentUserProfile(String email);

    UserResponse updateCurrentUserProfile(
            String email,
            String name,
            String phone,
            MultipartFile profileImage
    );
}