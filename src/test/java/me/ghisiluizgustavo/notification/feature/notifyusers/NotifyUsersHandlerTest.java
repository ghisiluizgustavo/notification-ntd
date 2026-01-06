package me.ghisiluizgustavo.notification.feature.notifyusers;

import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationEntityJpa;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationRepository;
import me.ghisiluizgustavo.user.domain.User;
import me.ghisiluizgustavo.user.infrastructure.database.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotifyUsersHandlerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailNotificationStrategy emailStrategy;

    @Mock
    private SmsNotificationStrategy smsStrategy;

    @Mock
    private PushNotificationStrategy pushStrategy;

    @InjectMocks
    private NotifyUsersHandler handler;

    @Captor
    private ArgumentCaptor<NotificationEntityJpa> notificationCaptor;

    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        final var strategies = List.of(emailStrategy, smsStrategy, pushStrategy);
        handler = new NotifyUsersHandler(notificationRepository, userRepository, strategies);

        when(emailStrategy.supports(NotificationType.EMAIL)).thenReturn(true);
        when(emailStrategy.supports(NotificationType.SMS)).thenReturn(false);
        when(emailStrategy.supports(NotificationType.PUSH)).thenReturn(false);

        when(smsStrategy.supports(NotificationType.SMS)).thenReturn(true);
        when(smsStrategy.supports(NotificationType.EMAIL)).thenReturn(false);
        when(smsStrategy.supports(NotificationType.PUSH)).thenReturn(false);

        when(pushStrategy.supports(NotificationType.PUSH)).thenReturn(true);
        when(pushStrategy.supports(NotificationType.EMAIL)).thenReturn(false);
        when(pushStrategy.supports(NotificationType.SMS)).thenReturn(false);

        testUsers = List.of(
            new User(
                1,
                "Alice",
                "alice@test.com",
                "+1-555-0101",
                List.of(NotificationCategory.SPORTS),
                List.of(NotificationType.EMAIL, NotificationType.PUSH),
                LocalDateTime.now(),
                LocalDateTime.now()
            ),
            new User(
                2,
                "Bob",
                "bob@test.com",
                "+1-555-0102",
                List.of(NotificationCategory.FINANCIAL),
                List.of(NotificationType.SMS),
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        );
    }

    @Test
    void shouldNotifySubscribedUsers_whenCategoryMatchesSubscription() {
        when(userRepository.findAll()).thenReturn(testUsers);

        final var request = new NotificationRequest(
            NotificationCategory.SPORTS,
            "Test content"
        );

        handler.handle(request);

        verify(emailStrategy, times(1)).send(any(User.class), any());
        verify(pushStrategy, times(1)).send(any(User.class), any());
        verify(smsStrategy, never()).send(any(User.class), any());
        verify(notificationRepository, times(2)).save(any(NotificationEntityJpa.class));
    }

    @Test
    void shouldNotNotifyUsers_whenNoUsersSubscribedToCategory() {
        when(userRepository.findAll()).thenReturn(testUsers);

        final var request = new NotificationRequest(
            NotificationCategory.MOVIES,
            "Test content"
        );

        handler.handle(request);

        verify(emailStrategy, never()).send(any(User.class), any());
        verify(smsStrategy, never()).send(any(User.class), any());
        verify(pushStrategy, never()).send(any(User.class), any());
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void shouldSendToAllUserChannels() {
        final var multiChannelUser = new User(
            3,
            "Carol",
            "carol@test.com",
            "+1-555-0103",
            List.of(NotificationCategory.SPORTS),
            List.of(NotificationType.EMAIL, NotificationType.SMS, NotificationType.PUSH),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(userRepository.findAll()).thenReturn(List.of(multiChannelUser));

        final var request = new NotificationRequest(
            NotificationCategory.SPORTS,
            "Test content"
        );

        handler.handle(request);

        verify(emailStrategy, times(1)).send(eq(multiChannelUser), any());
        verify(smsStrategy, times(1)).send(eq(multiChannelUser), any());
        verify(pushStrategy, times(1)).send(eq(multiChannelUser), any());
        verify(notificationRepository, times(3)).save(any(NotificationEntityJpa.class));
    }

    @Test
    void shouldSaveNotificationWithCorrectStatus_whenSendSucceeds() {
        when(userRepository.findAll()).thenReturn(List.of(testUsers.getFirst()));
        doNothing().when(emailStrategy).send(any(), any());

        final var request = new NotificationRequest(
            NotificationCategory.SPORTS,
            "Test content"
        );

        handler.handle(request);

        verify(notificationRepository, atLeastOnce()).save(notificationCaptor.capture());

        final var savedNotification = notificationCaptor.getValue();
        assertEquals("SENT", savedNotification.getStatus().name());
    }

    @Test
    void shouldSaveNotificationWithFailedStatus_whenSendThrowsException() {
        when(userRepository.findAll()).thenReturn(List.of(testUsers.getFirst()));
        doThrow(new RuntimeException("Send failed")).when(emailStrategy).send(any(), any());

        final var request = new NotificationRequest(
            NotificationCategory.SPORTS,
            "Test content"
        );

        handler.handle(request);

        verify(notificationRepository, atLeastOnce()).save(notificationCaptor.capture());
        
        final var savedNotifications = notificationCaptor.getAllValues();
        assertTrue(savedNotifications.stream()
            .anyMatch(n -> "FAILED".equals(n.getStatus().name())));
    }
}
