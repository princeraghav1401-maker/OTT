package com.streamverse.backend.controller;

import com.streamverse.backend.dto.request.SeasonRequest;
import com.streamverse.backend.dto.response.SeasonResponse;
import com.streamverse.backend.service.SeasonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class SeasonController {

    private final SeasonService seasonService;

    @PostMapping
    public ResponseEntity<SeasonResponse> createSeason(
            @Valid @RequestBody SeasonRequest request
    ) {
        return ResponseEntity.ok(seasonService.createSeason(request));
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<SeasonResponse>> getSeasonsBySeries(
            @PathVariable Long seriesId
    ) {
        return ResponseEntity.ok(seasonService.getSeasonsBySeries(seriesId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeasonResponse> getSeasonById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(seasonService.getSeasonById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeason(
            @PathVariable Long id
    ) {
        seasonService.deleteSeason(id);
        return ResponseEntity.ok("Season deleted successfully");
    }
}