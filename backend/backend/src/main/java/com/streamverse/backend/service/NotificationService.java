package com.streamverse.backend.service;

import com.streamverse.backend.dto.response.NotificationResponse;

import java.util.List;
import java.util.Map;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications(String userEmail);

    List<NotificationResponse> getUnreadNotifications(String userEmail);

    Map<String, Long> getUnreadCount(String userEmail);

    void markAsRead(String userEmail, Long notificationId);

    void markAllAsRead(String userEmail);

    NotificationResponse createNotification(
            Long userId,
            String title,
            String message,
            String type,
            Long referenceId
    );
}