package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.VideoPlayResponse;

public interface VideoService {

    VideoPlayResponse getMovieVideo(Long movieId, String userEmail);
}