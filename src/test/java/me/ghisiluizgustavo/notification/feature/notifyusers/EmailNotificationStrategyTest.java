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

class EmailNotificationStrategyTest {

    private EmailNotificationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new EmailNotificationStrategy();
    }

    @Test
    void shouldSupportEmailType() {
        assertTrue(strategy.supports(NotificationType.EMAIL));
    }

    @Test
    void shouldSendEmailNotification_withoutException() {
        final var user = new User(
            1,
            "Test User",
            "test@email.com",
            "+1-555-0100",
            List.of(NotificationCategory.SPORTS),
            List.of(NotificationType.EMAIL),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        final var notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertDoesNotThrow(() -> strategy.send(user, notification));
    }
}
