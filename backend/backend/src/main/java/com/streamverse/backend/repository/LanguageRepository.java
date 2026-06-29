package com.streamverse.backend.repository;

import com.streamverse.backend.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByName(String name);

    Optional<Language> findByCode(String code);

    boolean existsByName(String name);

    List<Language> findByActiveTrue();
}