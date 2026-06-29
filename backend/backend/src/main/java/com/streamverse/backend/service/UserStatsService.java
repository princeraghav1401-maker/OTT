package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.UserStatsResponse;

public interface UserStatsService {

    UserStatsResponse getMyStats(String userEmail);
}