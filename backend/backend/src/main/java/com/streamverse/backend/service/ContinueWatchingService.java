package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.ContinueWatchingResponse;

import java.util.List;

public interface ContinueWatchingService {

    List<ContinueWatchingResponse> getContinueWatching(String userEmail);
}