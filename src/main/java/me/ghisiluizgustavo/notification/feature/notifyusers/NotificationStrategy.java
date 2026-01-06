package me.ghisiluizgustavo.notification.feature.notifyusers;

import me.ghisiluizgustavo.notification.domain.Notification;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import me.ghisiluizgustavo.user.domain.User;

public interface NotificationStrategy {
    void send(User user, Notification notification);

    boolean supports(NotificationType type);
}
