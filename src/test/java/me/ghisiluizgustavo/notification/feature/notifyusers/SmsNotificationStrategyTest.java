package me.ghisiluizgustavo.notification.feature.notifyusers;

import me.ghisiluizgustavo.notification.domain.Notification;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import me.ghisiluizgustavo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmsNotificationStrategyTest {

    private SmsNotificationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SmsNotificationStrategy();
    }

    @Test
    void shouldSupportSmsType() {
        assertTrue(strategy.supports(NotificationType.SMS));
    }

    @Test
    void shouldSendSmsNotification_withoutException() {
        final var user = new User(
            1,
            "Test User",
            "test@email.com",
            "+1-555-0100",
            List.of(NotificationCategory.SPORTS),
            List.of(NotificationType.SMS),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        final var notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.SMS,
            "Test content"
        );

        assertDoesNotThrow(() -> strategy.send(user, notification));
    }
}
