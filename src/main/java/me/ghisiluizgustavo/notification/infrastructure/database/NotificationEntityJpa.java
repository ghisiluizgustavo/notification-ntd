package me.ghisiluizgustavo.notification.infrastructure.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ghisiluizgustavo.notification.domain.Notification;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationStatus;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationEntityJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private NotificationCategory category;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    private String content;
    private Integer userId;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static NotificationEntityJpa fromDomain(Notification notification) {
        return new NotificationEntityJpa(
            notification.getId(),
            notification.getCategory(),
            notification.getType(),
            notification.getStatus(),
            notification.getContent(),
            notification.getUserId(),
            notification.getCreatedAt(),
            notification.getUpdatedAt()
        );
    }

}
