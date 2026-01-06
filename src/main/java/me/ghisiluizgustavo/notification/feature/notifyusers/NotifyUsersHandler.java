package me.ghisiluizgustavo.notification.feature.notifyusers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ghisiluizgustavo.notification.domain.Notification;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationStatus;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationEntityJpa;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationRepository;
import me.ghisiluizgustavo.user.infrastructure.database.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyUsersHandler {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final List<NotificationStrategy> strategies;

    public void handle(NotificationRequest request) {
        final NotificationCategory category = request.category();
        final String content = request.content();

        log.info("Starting notification process for category: {}", category);

        final var subscribedUsers = userRepository.findAll()
            .stream()
            .filter(user -> user.subscribedCategories().contains(category))
            .toList();

        log.info("Found {} subscribed users for category: {}",
            subscribedUsers.size(),
            category
        );

        if (subscribedUsers.isEmpty()) {
            log.warn("No users subscribed to category: {}", category);
            return;
        }

        subscribedUsers.forEach(user -> {
            log.info("Processing user: {}", user.name());

            user.channels().forEach(channel -> strategies.stream()
                .filter(strategy -> strategy.supports(channel))
                .forEach(strategy -> {
                    final var notification = Notification.create(category, channel, content);
                    notification.setUserId(user.id());

                    try {
                        strategy.send(user, notification);
                        log.info("Sent via {} to {}", channel, user.name());
                        notification.updateStatus(NotificationStatus.SENT);
                    } catch (Exception e) {
                        log.error("Failed to send notification to {} via {}", user.name(), channel, e);
                        notification.updateStatus(NotificationStatus.FAILED);
                    }

                    notificationRepository.save(NotificationEntityJpa.fromDomain(notification));
                }));
        });

        log.info("Notification process completed! Sent to {} users", subscribedUsers.size());
    }
}
