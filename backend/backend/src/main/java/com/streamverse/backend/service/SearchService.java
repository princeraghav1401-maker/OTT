package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.SearchResponse;

public interface SearchService {

    SearchResponse search(String query);
}