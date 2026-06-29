package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.YoutubeVideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YouTubeService {

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<YoutubeVideoResponse> searchVideos(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = "https://www.googleapis.com/youtube/v3/search"
                    + "?part=snippet"
                    + "&type=video"
                    + "&maxResults=12"
                    + "&q=" + encodedQuery
                    + "&key=" + youtubeApiKey;

            Map response = restTemplate.getForObject(url, Map.class);

            List<Map<String, Object>> items =
                    (List<Map<String, Object>>) response.get("items");

            List<YoutubeVideoResponse> videos = new ArrayList<>();

            if (items == null) return videos;

            for (Map<String, Object> item : items) {
                Map<String, Object> id = (Map<String, Object>) item.get("id");
                Map<String, Object> snippet = (Map<String, Object>) item.get("snippet");
                Map<String, Object> thumbnails =
                        (Map<String, Object>) snippet.get("thumbnails");

                Map<String, Object> high =
                        (Map<String, Object>) thumbnails.get("high");

                String videoId = String.valueOf(id.get("videoId"));

                videos.add(
                        YoutubeVideoResponse.builder()
                                .videoId(videoId)
                                .title(String.valueOf(snippet.get("title")))
                                .description(String.valueOf(snippet.get("description")))
                                .channelTitle(String.valueOf(snippet.get("channelTitle")))
                                .thumbnailUrl(String.valueOf(high.get("url")))
                                .videoUrl("https://www.youtube.com/watch?v=" + videoId)
                                .build()
                );
            }

            return videos;

        } catch (Exception e) {
            throw new RuntimeException("Unable to fetch YouTube videos");
        }
    }
}