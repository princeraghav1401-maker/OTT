package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.ContinueWatchingResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.entity.WatchHistory;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.repository.WatchHistoryRepository;
import com.streamverse.backend.service.ContinueWatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContinueWatchingServiceImpl implements ContinueWatchingService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final UserRepository userRepository;

    @Override
    public List<ContinueWatchingResponse> getContinueWatching(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return watchHistoryRepository
                .findByUserIdOrderByLastWatchedAtDesc(user.getId())
                .stream()
                .filter(history -> !Boolean.TRUE.equals(history.getCompleted()))
                .filter(history -> history.getMovie() != null)
                .map(this::mapToResponse)
                .toList();
    }

    private ContinueWatchingResponse mapToResponse(WatchHistory history) {

        Movie movie = history.getMovie();

        Integer watchedSeconds = history.getWatchedSeconds() == null
                ? 0
                : history.getWatchedSeconds();

        Integer durationSeconds = history.getTotalSeconds() == null
                ? 0
                : history.getTotalSeconds();

        Double progress = durationSeconds > 0
                ? (watchedSeconds * 100.0) / durationSeconds
                : 0.0;

        return ContinueWatchingResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())

                .thumbnail(movie.getThumbnailUrl())
                .watchedSeconds(watchedSeconds)
                .durationSeconds(durationSeconds)
                .progressPercentage(progress)
                .build();
    }
}