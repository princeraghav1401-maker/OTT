package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.request.MovieRequest;
import com.streamverse.backend.dto.response.MovieResponse;
import com.streamverse.backend.entity.Category;
import com.streamverse.backend.entity.Genre;
import com.streamverse.backend.entity.Language;
import com.streamverse.backend.entity.Movie;
import com.streamverse.backend.repository.CategoryRepository;
import com.streamverse.backend.repository.GenreRepository;
import com.streamverse.backend.repository.LanguageRepository;
import com.streamverse.backend.repository.MovieRepository;
import com.streamverse.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final GenreRepository genreRepository;

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findByDeletedFalseAndApprovedTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public MovieResponse getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .filter(m -> Boolean.TRUE.equals(m.getApproved()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return mapToResponse(movie);
    }

    @Override
    public List<MovieResponse> searchMovies(String keyword) {
        return movieRepository
                .findByTitleContainingIgnoreCaseAndDeletedFalseAndApprovedTrue(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<MovieResponse> filterMovies(Long categoryId, Long languageId, Integer releaseYear) {

        if (categoryId != null) {
            return movieRepository.findByCategoryIdAndDeletedFalseAndApprovedTrue(categoryId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        if (languageId != null) {
            return movieRepository.findByLanguageIdAndDeletedFalseAndApprovedTrue(languageId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        if (releaseYear != null) {
            return movieRepository.findByReleaseYearAndDeletedFalseAndApprovedTrue(releaseYear)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();
        }

        return getAllMovies();
    }

    @Override
    public MovieResponse createMovie(MovieRequest request) {

        if (movieRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Movie slug already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Language language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Language not found"));

        Set<Genre> genres = getGenresFromIds(request.getGenreIds());

        Movie movie = Movie.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .description(request.getDescription())
                .cast(request.getCast())
                .director(request.getDirector())
                .releaseYear(request.getReleaseYear())
                .durationMinutes(request.getDurationMinutes())
                .ageRating(request.getAgeRating())
                .thumbnailUrl(request.getThumbnailUrl())
                .bannerUrl(request.getBannerUrl())
                .trailerUrl(request.getTrailerUrl())
                .premium(Boolean.TRUE.equals(request.getPremium()))
                .approved(true)
                .deleted(false)
                .views(0L)
                .category(category)
                .language(language)
                .genres(genres)
                .build();

        Movie savedMovie = movieRepository.save(movie);

        return mapToResponse(savedMovie);
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieRequest request) {

        Movie movie = movieRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (!movie.getSlug().equals(request.getSlug())
                && movieRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Movie slug already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Language language = languageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new RuntimeException("Language not found"));

        Set<Genre> genres = getGenresFromIds(request.getGenreIds());

        movie.setTitle(request.getTitle());
        movie.setSlug(request.getSlug());
        movie.setDescription(request.getDescription());
        movie.setCast(request.getCast());
        movie.setDirector(request.getDirector());
        movie.setReleaseYear(request.getReleaseYear());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setAgeRating(request.getAgeRating());
        movie.setThumbnailUrl(request.getThumbnailUrl());
        movie.setBannerUrl(request.getBannerUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setPremium(Boolean.TRUE.equals(request.getPremium()));
        movie.setCategory(category);
        movie.setLanguage(language);
        movie.setGenres(genres);

        Movie updatedMovie = movieRepository.save(movie);

        return mapToResponse(updatedMovie);
    }

    @Override
    public void deleteMovie(Long id) {

        Movie movie = movieRepository.findById(id)
                .filter(m -> !Boolean.TRUE.equals(m.getDeleted()))
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    private Set<Genre> getGenresFromIds(List<Long> genreIds) {

        if (genreIds == null || genreIds.isEmpty()) {
            return new HashSet<>();
        }

        return genreIds.stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new RuntimeException("Genre not found with id: " + genreId)))
                .collect(Collectors.toSet());
    }

    private MovieResponse mapToResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .slug(movie.getSlug())
                .description(movie.getDescription())
                .cast(movie.getCast())
                .director(movie.getDirector())
                .releaseYear(movie.getReleaseYear())
                .durationMinutes(movie.getDurationMinutes())
                .ageRating(movie.getAgeRating())
                .thumbnailUrl(movie.getThumbnailUrl())
                .bannerUrl(movie.getBannerUrl())
                .trailerUrl(movie.getTrailerUrl())
                .premium(movie.getPremium())
                .views(movie.getViews())
                .category(movie.getCategory() != null ? movie.getCategory().getName() : null)
                .language(movie.getLanguage() != null ? movie.getLanguage().getName() : null)
                .genres(
                        movie.getGenres() != null
                                ? movie.getGenres()
                                .stream()
                                .map(Genre::getName)
                                .toList()
                                : List.of()
                )
                .build();
    }
}