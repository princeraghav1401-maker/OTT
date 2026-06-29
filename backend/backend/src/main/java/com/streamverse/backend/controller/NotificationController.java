package com.streamverse.backend.controller;

import com.streamverse.backend.dto.response.NotificationResponse;
import com.streamverse.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getMyNotifications(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                notificationService.getMyNotifications(authentication.getName())
        );
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(authentication.getName())
        );
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                notificationService.getUnreadCount(authentication.getName())
        );
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id,
            Authentication authentication
    ) {
        notificationService.markAsRead(authentication.getName(), id);
        return ResponseEntity.ok("Notification marked as read");
    }

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(
            Authentication authentication
    ) {
        notificationService.markAllAsRead(authentication.getName());
        return ResponseEntity.ok("All notifications marked as read");
    }
}