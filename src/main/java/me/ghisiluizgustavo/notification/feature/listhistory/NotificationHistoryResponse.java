package me.ghisiluizgustavo.notification.feature.listhistory;

import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationStatus;
import me.ghisiluizgustavo.notification.domain.NotificationType;

import java.time.LocalDateTime;

public record NotificationHistoryResponse(
    Integer id,
    NotificationCategory category,
    NotificationType type,
    String content,
    NotificationStatus status,
    Integer userId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
