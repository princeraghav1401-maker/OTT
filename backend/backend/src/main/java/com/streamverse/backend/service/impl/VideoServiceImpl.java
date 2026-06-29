package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.VideoPlayResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.entity.VideoFile;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.repository.VideoFileRepository;
import com.streamverse.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final MovieRepository movieRepository;
    private final VideoFileRepository videoFileRepository;
    private final UserRepository userRepository;

    @Override
    public VideoPlayResponse getMovieVideo(Long movieId, String userEmail) {

        Movie movie = movieRepository.findById(movieId)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (Boolean.TRUE.equals(movie.getPremium())) {
            checkUserHasPremiumAccess(userEmail);
        }

        VideoFile videoFile = videoFileRepository
                .findByMovieIdAndContentType(movieId, "MOVIE")
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Video file not found"));

        return VideoPlayResponse.builder()
                .movieId(movie.getId())
                .title(movie.getTitle())
                .premium(movie.getPremium())
                .videoUrl(videoFile.getFileUrl())
                .quality(videoFile.getQuality())
                .durationSeconds(videoFile.getDurationSeconds())
                .subtitlesUrl(videoFile.getSubtitlesUrl())
                .build();
    }

    private void checkUserHasPremiumAccess(String userEmail) {

        if (userEmail == null || userEmail.isBlank()) {
            throw new RuntimeException("Login required for premium content");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == null) {
            throw new RuntimeException("Premium access denied");
        }
    }
}