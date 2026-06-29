package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.UpdateProfileRequest;
import com.streamverse.backend.dto.response.UserProfileResponse;

public interface ProfileService {

    UserProfileResponse getProfile(String userEmail);

    UserProfileResponse updateProfile(String userEmail, UpdateProfileRequest request);
}