package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.GenreResponse;
import com.streamverse.backend.entity.Genre;
import com.streamverse.backend.repository.GenreRepository;
import com.streamverse.backend.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<GenreResponse> getAllActiveGenres() {
        return genreRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private GenreResponse mapToResponse(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}