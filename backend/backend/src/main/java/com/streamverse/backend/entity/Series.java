package com.streamverse.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String cast;

    private String director;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "age_rating")
    private String ageRating;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "trailer_url")
    private String trailerUrl;

    @Builder.Default
    @Column(nullable = false)
    private Boolean premium = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean approved = true;

    @Builder.Default
    @Column(nullable = false)
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "series_genres",
            joinColumns = @JoinColumn(name = "series_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        if (premium == null) {
            premium = false;
        }

        if (approved == null) {
            approved = true;
        }

        if (deleted == null) {
            deleted = false;
        }

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}