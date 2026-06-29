package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.VideoFileRequest;
import com.streamverse.backend.dto.response.VideoFileResponse;
import com.streamverse.backend.service.VideoFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video-files")
@RequiredArgsConstructor
public class VideoFileController {

    private final VideoFileService videoFileService;

    @PostMapping
    public VideoFileResponse create(@RequestBody VideoFileRequest request) {
        return videoFileService.create(request);
    }

    @GetMapping("/movie/{movieId}")
    public VideoFileResponse getMovieVideo(@PathVariable Long movieId) {
        return videoFileService.getByMovie(movieId);
    }

    @GetMapping("/episode/{episodeId}")
    public VideoFileResponse getEpisodeVideo(@PathVariable Long episodeId) {
        return videoFileService.getByEpisode(episodeId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        videoFileService.delete(id);
        return "Video deleted successfully";
    }

    @GetMapping("/{id}")
    public VideoFileResponse getById(@PathVariable Long id) {
        return videoFileService.getById(id);
    }
}