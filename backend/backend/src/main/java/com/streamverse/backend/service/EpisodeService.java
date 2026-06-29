package com.streamverse.backend.service;

import com.streamverse.backend.dto.request.EpisodeRequest;
import com.streamverse.backend.dto.response.EpisodeResponse;

import java.util.List;

public interface EpisodeService {

    EpisodeResponse createEpisode(EpisodeRequest request);

    EpisodeResponse getEpisodeById(Long id);

    List<EpisodeResponse> getEpisodesBySeries(Long seriesId);

    void deleteEpisode(Long id);
}