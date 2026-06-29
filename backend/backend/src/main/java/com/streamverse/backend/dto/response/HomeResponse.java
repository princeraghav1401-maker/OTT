package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HomeResponse {

    private List<MovieResponse> trendingMovies;
    private List<MovieResponse> latestMovies;
    private List<MovieResponse> premiumMovies;

    private List<SeriesResponse> latestSeries;

    private List<ContinueWatchingResponse> continueWatching;
    private List<WatchlistResponse> myWatchlist;


    private List<YoutubeVideoResponse> latestTrailers;
    private List<YoutubeVideoResponse> trendingYoutube;
    private List<YoutubeVideoResponse> latestSongs;
}