package com.streamverse.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "watch_history",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "movie_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who watched
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Movie watched
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "watched_seconds", nullable = false)
    private Integer watchedSeconds;

    @Column(name = "total_seconds", nullable = false)
    private Integer totalSeconds;

    @Column(
            name = "progress_percentage",
            nullable = false,
            columnDefinition = "DOUBLE"
    )
    private Double progressPercentage;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "last_watched_at", nullable = false)
    private LocalDateTime lastWatchedAt;

    @PrePersist
    public void onCreate() {

        if (watchedSeconds == null) {
            watchedSeconds = 0;
        }

        if (totalSeconds == null) {
            totalSeconds = 0;
        }

        if (progressPercentage == null) {
            progressPercentage = 0.0;
        }

        if (completed == null) {
            completed = false;
        }

        lastWatchedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        lastWatchedAt = LocalDateTime.now();
    }
}