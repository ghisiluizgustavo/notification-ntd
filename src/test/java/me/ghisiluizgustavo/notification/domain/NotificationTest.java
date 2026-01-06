package me.ghisiluizgustavo.notification.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void shouldCreateNotification_withValidParameters() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertNotNull(notification);
        assertEquals(NotificationCategory.SPORTS, notification.getCategory());
        assertEquals(NotificationType.EMAIL, notification.getType());
        assertEquals("Test content", notification.getContent());
    }

    @Test
    void shouldSetStatusToPending_whenNotificationIsCreated() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertEquals(NotificationStatus.PENDING, notification.getStatus());
    }

    @Test
    void shouldInitializeTimestamps_whenNotificationIsCreated() {
        LocalDateTime before = LocalDateTime.now();

        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(notification.getCreatedAt());
        assertNotNull(notification.getUpdatedAt());
        assertTrue(notification.getCreatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(notification.getCreatedAt().isBefore(after.plusSeconds(1)));
        assertTrue(notification.getUpdatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(notification.getUpdatedAt().isBefore(after.plusSeconds(1)));
    }

    @Test
    void shouldThrowException_whenCategoryIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Notification.create(null, NotificationType.EMAIL, "Test content")
        );
        assertEquals("Category cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenTypeIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Notification.create(NotificationCategory.SPORTS, null, "Test content")
        );
        assertEquals("Type cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenContentIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Notification.create(NotificationCategory.SPORTS, NotificationType.EMAIL, null)
        );
        assertEquals("Content cannot be null", exception.getMessage());
    }

    @Test
    void shouldUpdateStatus_whenUpdateStatusIsCalled() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );
        assertEquals(NotificationStatus.PENDING, notification.getStatus());

        notification.updateStatus(NotificationStatus.SENT);

        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    void shouldUpdateTimestamp_whenStatusIsUpdated() throws InterruptedException {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );
        LocalDateTime originalUpdatedAt = notification.getUpdatedAt();
        
        Thread.sleep(10);

        notification.updateStatus(NotificationStatus.SENT);

        assertTrue(notification.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    void shouldSetUserId_whenValidIdIsProvided() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        notification.setUserId(1);

        assertEquals(1, notification.getUserId());
    }

    @Test
    void shouldThrowException_whenUserIdIsNull() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notification.setUserId(null)
        );
        assertEquals("User id cannot be null and different of zero", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenUserIdIsZero() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notification.setUserId(0)
        );
        assertEquals("User id cannot be null and different of zero", exception.getMessage());
    }

    @Test
    void shouldThrowException_whenUserIdIsNegative() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> notification.setUserId(-1)
        );
        assertEquals("User id cannot be null and different of zero", exception.getMessage());
    }

    @Test
    void shouldCreateNotification_withAllCategories() {
        Notification sports = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Sports content"
        );
        Notification financial = Notification.create(
            NotificationCategory.FINANCIAL,
            NotificationType.SMS,
            "Financial content"
        );
        Notification movies = Notification.create(
            NotificationCategory.MOVIES,
            NotificationType.PUSH,
            "Movies content"
        );

        assertEquals(NotificationCategory.SPORTS, sports.getCategory());
        assertEquals(NotificationCategory.FINANCIAL, financial.getCategory());
        assertEquals(NotificationCategory.MOVIES, movies.getCategory());
    }

    @Test
    void shouldCreateNotification_withAllTypes() {
        Notification email = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test"
        );
        Notification sms = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.SMS,
            "Test"
        );
        Notification push = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.PUSH,
            "Test"
        );

        assertEquals(NotificationType.EMAIL, email.getType());
        assertEquals(NotificationType.SMS, sms.getType());
        assertEquals(NotificationType.PUSH, push.getType());
    }

    @Test
    void shouldAllowMultipleStatusUpdates() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertEquals(NotificationStatus.PENDING, notification.getStatus());

        notification.updateStatus(NotificationStatus.SENT);
        assertEquals(NotificationStatus.SENT, notification.getStatus());

        notification.updateStatus(NotificationStatus.FAILED);
        assertEquals(NotificationStatus.FAILED, notification.getStatus());

        notification.updateStatus(NotificationStatus.PENDING);
        assertEquals(NotificationStatus.PENDING, notification.getStatus());
    }

    @Test
    void shouldHaveNullId_whenNotificationIsCreated() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertNull(notification.getId());
    }

    @Test
    void shouldHaveNullUserId_whenNotificationIsCreated() {
        Notification notification = Notification.create(
            NotificationCategory.SPORTS,
            NotificationType.EMAIL,
            "Test content"
        );

        assertNull(notification.getUserId());
    }
}
