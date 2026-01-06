package me.ghisiluizgustavo.user.domain;

import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

public record User(
    Integer id,
    String name,
    String email,
    String phoneNumber,
    List<NotificationCategory> subscribedCategories,
    List<NotificationType> channels,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
