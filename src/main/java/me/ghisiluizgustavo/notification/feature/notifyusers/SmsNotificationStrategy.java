package me.ghisiluizgustavo.notification.feature.notifyusers;

import lombok.extern.slf4j.Slf4j;
import me.ghisiluizgustavo.notification.domain.Notification;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import me.ghisiluizgustavo.user.domain.User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsNotificationStrategy implements NotificationStrategy {

    @Override
    public void send(User user, Notification notification) {
        log.info("Sending SMS notification to: {} ({})", user.name(), user.phoneNumber());
        log.info("   Category: {} | Type: {} | Content: {}", 
            notification.getCategory(),
            notification.getType(),
            notification.getContent()
        );
    }

    @Override
    public boolean supports(NotificationType type) {
        return NotificationType.SMS == type;
    }

}
