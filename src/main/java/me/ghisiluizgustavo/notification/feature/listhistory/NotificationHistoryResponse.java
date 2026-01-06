package me.ghisiluizgustavo.notification.feature.listhistory;

import io.swagger.v3.oas.annotations.media.Schema;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationStatus;
import me.ghisiluizgustavo.notification.domain.NotificationType;

import java.time.LocalDateTime;

@Schema(description = "Notification")
public record NotificationHistoryResponse(
    @Schema(description = "Notification ID", example = "1")
    Integer id,
    
    @Schema(description = "Notification category", example = "SPORTS")
    NotificationCategory category,
    
    @Schema(description = "Notification type/channel", example = "EMAIL")
    NotificationType type,
    
    @Schema(description = "Notification content", example = "Big game tonight!")
    String content,
    
    @Schema(description = "Notification status", example = "SENT")
    NotificationStatus status,
    
    @Schema(description = "User ID who received the notification", example = "1")
    Integer userId,
    
    @Schema(description = "Creation timestamp", example = "2026-01-06T14:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Last update timestamp", example = "2026-01-06T14:30:00")
    LocalDateTime updatedAt
) {
}
