package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.SearchResponse;
import com.streamverse.backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam(name = "q", required = false) String query
    ) {
        return ResponseEntity.ok(searchService.search(query));
    }
}