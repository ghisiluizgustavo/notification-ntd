package me.ghisiluizgustavo.notification.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Notification {
    private Integer id;
    private NotificationCategory category;
    private NotificationType type;
    private String content;
    private NotificationStatus status;
    private Integer userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private Notification(
        NotificationCategory category,
        NotificationType type,
        String content
    ) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        this.category = category;
        this.type = type;
        this.content = content;
        this.status = NotificationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public static Notification create(
        NotificationCategory category,
        NotificationType type,
        String content
    ) {
        return new Notification(category, type, content);
    }
    
    public void updateStatus(NotificationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public void setUserId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User id cannot be null and different of zero");
        }
        this.userId = id;
    }
}
