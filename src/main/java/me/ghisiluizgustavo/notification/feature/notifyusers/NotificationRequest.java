package me.ghisiluizgustavo.notification.feature.notifyusers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;

public record NotificationRequest(
        @NotNull(message = "Category is required")
        NotificationCategory category,
        
        @NotBlank(message = "Content is required and cannot be blank")
        @Size(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
        String content
) { }