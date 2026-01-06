package me.ghisiluizgustavo.notification.feature.listhistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationEntityJpa;
import me.ghisiluizgustavo.notification.infrastructure.database.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListNotificationHistoryHandler {

    private final NotificationRepository notificationRepository;

    public List<NotificationHistoryResponse> handle() {
        log.info("Fetching notification history");
        
        List<NotificationEntityJpa> notifications = notificationRepository
            .findAllByOrderByCreatedAtDesc();
        
        log.info("Found {} notifications in history", notifications.size());
        
        return notifications.stream()
            .map(this::toResponse)
            .toList();
    }

    private NotificationHistoryResponse toResponse(NotificationEntityJpa entity) {
        return new NotificationHistoryResponse(
            entity.getId(),
            entity.getCategory(),
            entity.getType(),
            entity.getContent(),
            entity.getStatus(),
            entity.getUserId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
