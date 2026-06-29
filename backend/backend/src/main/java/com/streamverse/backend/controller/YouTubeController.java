package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.YoutubeVideoResponse;
import com.streamverse.backend.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/youtube")
@RequiredArgsConstructor
public class YouTubeController {

    private final YouTubeService youTubeService;

    @GetMapping("/search")
    public List<YoutubeVideoResponse> search(@RequestParam String query) {
        return youTubeService.searchVideos(query);
    }
}