package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.VideoFileResponse;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.entity.VideoFile;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.repository.VideoFileRepository;
import com.streamverse.backend.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/videos")
@RequiredArgsConstructor
public class AdminVideoController {

    private final FileStorageService fileStorageService;
    private final MovieRepository movieRepository;
    private final VideoFileRepository videoFileRepository;

    @PostMapping("/movie/{movieId}")
    public ResponseEntity<VideoFileResponse> uploadMovieVideo(
            @PathVariable Long movieId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "MOVIE") String contentType,
            @RequestParam(defaultValue = "720p") String quality,
            @RequestParam(required = false) Integer durationSeconds,
            @RequestParam(required = false) String subtitlesUrl
    ) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        String fileUrl = fileStorageService.storeFile(file, "movies");

        VideoFile videoFile = VideoFile.builder()
                .contentType(contentType)
                .movie(movie)
                .fileName(file.getOriginalFilename())
                .filePath(fileUrl)
                .fileUrl(fileUrl)
                .quality(quality)
                .durationSeconds(durationSeconds)
                .fileSize(file.getSize())
                .subtitlesUrl(subtitlesUrl)
                .build();

        VideoFile saved = videoFileRepository.save(videoFile);

        return ResponseEntity.ok(mapToResponse(saved));
    }

    @PostMapping("/trailer/{movieId}")
    public ResponseEntity<VideoFileResponse> uploadMovieTrailer(
            @PathVariable Long movieId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "TRAILER") String contentType,
            @RequestParam(defaultValue = "720p") String quality,
            @RequestParam(required = false) Integer durationSeconds
    ) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        String fileUrl = fileStorageService.storeFile(file, "trailers");

        VideoFile videoFile = VideoFile.builder()
                .contentType(contentType)
                .movie(movie)
                .fileName(file.getOriginalFilename())
                .filePath(fileUrl)
                .fileUrl(fileUrl)
                .quality(quality)
                .durationSeconds(durationSeconds)
                .fileSize(file.getSize())
                .build();

        VideoFile saved = videoFileRepository.save(videoFile);

        return ResponseEntity.ok(mapToResponse(saved));
    }

    private VideoFileResponse mapToResponse(VideoFile videoFile) {
        return VideoFileResponse.builder()
                .id(videoFile.getId())
                .contentType(videoFile.getContentType())
                .movieId(videoFile.getMovie() != null ? videoFile.getMovie().getId() : null)
                .fileName(videoFile.getFileName())
                .filePath(videoFile.getFilePath())
                .fileUrl(videoFile.getFileUrl())
                .quality(videoFile.getQuality())
                .durationSeconds(videoFile.getDurationSeconds())
                .fileSize(videoFile.getFileSize())
                .subtitlesUrl(videoFile.getSubtitlesUrl())
                .build();
    }
}