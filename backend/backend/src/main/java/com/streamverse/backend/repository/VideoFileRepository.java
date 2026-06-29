package com.streamverse.backend.repository;

import com.streamverse.backend.entity.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {

    List<VideoFile> findByMovieId(Long movieId);

    Optional<VideoFile> findByMovieIdAndContentType(
            Long movieId,
            String contentType
    );
}