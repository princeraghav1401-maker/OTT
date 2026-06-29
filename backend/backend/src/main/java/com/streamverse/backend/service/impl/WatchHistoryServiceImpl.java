package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.WatchProgressRequest;
import com.streamverse.backend.dto.response.ContinueWatchingResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.entity.VideoFile;
import com.streamverse.backend.entity.WatchHistory;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.repository.VideoFileRepository;
import com.streamverse.backend.repository.WatchHistoryRepository;
import com.streamverse.backend.service.WatchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchHistoryServiceImpl implements WatchHistoryService {

    private final WatchHistoryRepository watchHistoryRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final VideoFileRepository videoFileRepository;

    @Override
    public ContinueWatchingResponse saveProgress(
            String userEmail,
            WatchProgressRequest request
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        VideoFile videoFile = videoFileRepository
                .findByMovieIdAndContentType(movie.getId(), "MOVIE")
                .stream()
                .findFirst()
                .orElse(null);

        Integer totalSeconds = videoFile != null && videoFile.getDurationSeconds() != null
                ? videoFile.getDurationSeconds()
                : 0;

        Double progressPercentage = calculateProgress(
                request.getWatchedSeconds(),
                totalSeconds
        );

        WatchHistory watchHistory = watchHistoryRepository
                .findByUserIdAndMovieId(user.getId(), movie.getId())
                .orElse(
                        WatchHistory.builder()
                                .user(user)
                                .movie(movie)
                                .build()
                );

        watchHistory.setWatchedSeconds(request.getWatchedSeconds());
        watchHistory.setTotalSeconds(totalSeconds);
        watchHistory.setProgressPercentage(progressPercentage);
        watchHistory.setCompleted(Boolean.TRUE.equals(request.getCompleted()));

        WatchHistory saved = watchHistoryRepository.save(watchHistory);

        return mapToResponse(saved);
    }

    @Override
    public List<ContinueWatchingResponse> getContinueWatching(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return watchHistoryRepository
                .findByUserIdOrderByLastWatchedAtDesc(user.getId())
                .stream()
                .filter(history -> !Boolean.TRUE.equals(history.getCompleted()))
                .map(this::mapToResponse)
                .toList();
    }

    private Double calculateProgress(Integer watchedSeconds, Integer totalSeconds) {
        if (watchedSeconds == null || totalSeconds == null || totalSeconds <= 0) {
            return 0.0;
        }

        double progress = (watchedSeconds * 100.0) / totalSeconds;

        return Math.min(progress, 100.0);
    }

    private ContinueWatchingResponse mapToResponse(WatchHistory history) {
        Movie movie = history.getMovie();

        return ContinueWatchingResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .thumbnail(movie.getThumbnailUrl())
                .watchedSeconds(history.getWatchedSeconds())
                .durationSeconds(history.getTotalSeconds())
                .progressPercentage(history.getProgressPercentage())
                .build();
    }
}