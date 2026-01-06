package me.ghisiluizgustavo.notification.feature.notifyusers;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;

@Schema(description = "Request to send a notification to subscribed users")
public record NotificationRequest(
        @Schema(description = "Notification category", example = "SPORTS")
        @NotNull(message = "Category is required")
        NotificationCategory category,
        
        @Schema(description = "Notification content/message", example = "Big game tonight at 8 PM!")
        @NotBlank(message = "Content is required and cannot be blank")
        @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
        String content
) { }