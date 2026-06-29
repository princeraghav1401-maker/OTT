package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.LanguageResponse;
import com.streamverse.backend.entity.Language;
import com.streamverse.backend.repository.LanguageRepository;
import com.streamverse.backend.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    @Override
    public List<LanguageResponse> getAllActiveLanguages() {
        return languageRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LanguageResponse mapToResponse(Language language) {
        return LanguageResponse.builder()
                .id(language.getId())
                .name(language.getName())
                .code(language.getCode())
                .build();
    }
}