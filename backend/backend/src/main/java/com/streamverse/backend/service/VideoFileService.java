package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.VideoFileRequest;
import com.streamverse.backend.dto.response.VideoFileResponse;

public interface VideoFileService {

    VideoFileResponse create(VideoFileRequest request);

    VideoFileResponse getById(Long id);

    VideoFileResponse getByMovie(Long movieId);

    VideoFileResponse getByEpisode(Long episodeId);

    void delete(Long id);
}