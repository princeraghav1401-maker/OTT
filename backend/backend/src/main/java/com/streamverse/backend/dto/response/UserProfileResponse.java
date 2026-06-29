package com.streamverse.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileResponse {

    private Long id;

    private String name;
    private String email;
    private String phone;
    private String role;

    private Boolean active;
    private Boolean deleted;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}