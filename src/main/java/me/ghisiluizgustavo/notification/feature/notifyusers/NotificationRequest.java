package me.ghisiluizgustavo.notification.feature.notifyusers;

import me.ghisiluizgustavo.notification.domain.NotificationCategory;

public record NotificationRequest(
        NotificationCategory category,
        String content
    ) {
    }