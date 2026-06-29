package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.VideoFileRequest;
import com.streamverse.backend.dto.response.VideoFileResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.VideoFile;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.VideoFileRepository;
import com.streamverse.backend.service.VideoFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoFileServiceImpl implements VideoFileService {

    private final VideoFileRepository videoFileRepository;
    private final MovieRepository movieRepository;

    @Override
    public VideoFileResponse create(VideoFileRequest request) {

        Movie movie = null;

        if (request.getMovieId() != null) {
            movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
        }

        VideoFile video = VideoFile.builder()
                .contentType(request.getContentType())
                .movie(movie)
                .fileName(request.getVideoUrl())
                .filePath(request.getVideoUrl())
                .fileUrl(request.getVideoUrl())
                .quality(request.getQuality())
                .durationSeconds(request.getDurationSeconds())
                .fileSize(0L)
                .subtitlesUrl(request.getSubtitleUrl())
                .build();

        VideoFile saved = videoFileRepository.save(video);

        return map(saved);
    }

    @Override
    public VideoFileResponse getByMovie(Long movieId) {

        VideoFile video = videoFileRepository.findByMovieId(movieId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Video not found"));

        return map(video);
    }

    @Override
    public VideoFileResponse getByEpisode(Long episodeId) {
        throw new RuntimeException("Episode video support abhi existing VideoFile entity me mapped nahi hai");
    }

    @Override
    public void delete(Long id) {

        VideoFile video = videoFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        videoFileRepository.delete(video);
    }

    private VideoFileResponse map(VideoFile video) {

        return VideoFileResponse.builder()
                .id(video.getId())
                .contentType(video.getContentType())
                .movieId(video.getMovie() != null ? video.getMovie().getId() : null)
                .fileName(video.getFileName())
                .filePath(video.getFilePath())
                .fileUrl(video.getFileUrl())
                .quality(video.getQuality())
                .durationSeconds(video.getDurationSeconds())
                .fileSize(video.getFileSize())
                .subtitlesUrl(video.getSubtitlesUrl())
                .build();
    }


    @Override
    public VideoFileResponse getById(Long id) {
        VideoFile video = videoFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        return map(video);
    }
}