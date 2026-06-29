package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.LanguageResponse;
import java.util.List;

public interface LanguageService {
    List<LanguageResponse> getAllActiveLanguages();
}