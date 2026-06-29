package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.EpisodeRequest;
import com.streamverse.backend.dto.response.EpisodeResponse;
import com.streamverse.backend.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @PostMapping
    public EpisodeResponse createEpisode(
            @RequestBody EpisodeRequest request
    ) {
        return episodeService.createEpisode(request);
    }

    @GetMapping("/{id}")
    public EpisodeResponse getEpisodeById(
            @PathVariable Long id
    ) {
        return episodeService.getEpisodeById(id);
    }

    @GetMapping("/series/{seriesId}")
    public List<EpisodeResponse> getEpisodesBySeries(
            @PathVariable Long seriesId
    ) {
        return episodeService.getEpisodesBySeries(seriesId);
    }

    @DeleteMapping("/{id}")
    public String deleteEpisode(
            @PathVariable Long id
    ) {

        episodeService.deleteEpisode(id);

        return "Episode deleted successfully";
    }
}