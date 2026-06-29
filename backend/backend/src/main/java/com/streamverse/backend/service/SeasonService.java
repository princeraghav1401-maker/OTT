package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.SeasonRequest;
import com.streamverse.backend.dto.response.SeasonResponse;

import java.util.List;

public interface SeasonService {

    SeasonResponse createSeason(SeasonRequest request);

    List<SeasonResponse> getSeasonsBySeries(Long seriesId);

    SeasonResponse getSeasonById(Long id);

    void deleteSeason(Long id);
}