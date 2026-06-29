package com.streamverse.backend.service.impl;

import com.streamverse.backend.dto.response.NotificationResponse;
import com.streamverse.backend.entity.Notification;
import com.streamverse.backend.entity.User;
import com.streamverse.backend.repository.NotificationRepository;
import com.streamverse.backend.repository.UserRepository;
import com.streamverse.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationResponse> getMyNotifications(String userEmail) {

        User user = getUserByEmail(userEmail);

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<NotificationResponse> getUnreadNotifications(String userEmail) {

        User user = getUserByEmail(userEmail);

        return notificationRepository
                .findByUserIdAndReadFalseOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Map<String, Long> getUnreadCount(String userEmail) {

        User user = getUserByEmail(userEmail);

        Long count = notificationRepository.countByUserIdAndReadFalse(user.getId());

        return Map.of("unreadCount", count);
    }

    @Override
    public void markAsRead(String userEmail, Long notificationId) {

        User user = getUserByEmail(userEmail);

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot update this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String userEmail) {

        User user = getUserByEmail(userEmail);

        List<Notification> notifications =
                notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(user.getId());

        notifications.forEach(notification -> notification.setRead(true));

        notificationRepository.saveAll(notifications);
    }

    @Override
    public NotificationResponse createNotification(
            Long userId,
            String title,
            String message,
            String type,
            Long referenceId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .type(type)
                .referenceId(referenceId)
                .read(false)
                .build();

        return mapToResponse(notificationRepository.save(notification));
    }

    private User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .referenceId(notification.getReferenceId())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}