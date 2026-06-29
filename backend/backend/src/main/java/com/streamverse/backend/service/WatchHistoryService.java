package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.WatchProgressRequest;
import com.streamverse.backend.dto.response.ContinueWatchingResponse;

import java.util.List;

public interface WatchHistoryService {

    ContinueWatchingResponse saveProgress(String userEmail, WatchProgressRequest request);

    List<ContinueWatchingResponse> getContinueWatching(String userEmail);
}