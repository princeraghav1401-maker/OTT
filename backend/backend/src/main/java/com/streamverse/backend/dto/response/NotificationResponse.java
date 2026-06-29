package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private Long id;

    private String title;
    private String message;
    private String type;

    private Long referenceId;

    private Boolean read;

    private LocalDateTime createdAt;
}