package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.GenreResponse;
import java.util.List;

public interface GenreService {
    List<GenreResponse> getAllActiveGenres();
}