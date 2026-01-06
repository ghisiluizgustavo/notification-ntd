package me.ghisiluizgustavo.user.infrastructure.database;

import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.domain.NotificationType;
import me.ghisiluizgustavo.user.domain.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = List.of(
        new User(
            1,
            "Alice Johnson",
            "alice.johnson@email.com",
            "+1-555-0101",
            List.of(NotificationCategory.SPORTS, NotificationCategory.MOVIES),
            List.of(NotificationType.EMAIL, NotificationType.PUSH),
            LocalDateTime.now().minusDays(30),
            LocalDateTime.now().minusDays(30)
        ),
        new User(
            2,
            "Bob Smith",
            "bob.smith@email.com",
            "+1-555-0102",
            List.of(NotificationCategory.FINANCIAL),
            List.of(NotificationType.EMAIL, NotificationType.SMS),
            LocalDateTime.now().minusDays(25),
            LocalDateTime.now().minusDays(25)
        ),
        new User(
            3,
            "Carol Williams",
            "carol.williams@email.com",
            "+1-555-0103",
            List.of(NotificationCategory.SPORTS, NotificationCategory.FINANCIAL, NotificationCategory.MOVIES),
            List.of(NotificationType.EMAIL, NotificationType.SMS, NotificationType.PUSH),
            LocalDateTime.now().minusDays(20),
            LocalDateTime.now().minusDays(20)
        ),
        new User(
            4,
            "David Brown",
            "david.brown@email.com",
            "+1-555-0104",
            List.of(NotificationCategory.MOVIES),
            List.of(NotificationType.PUSH),
            LocalDateTime.now().minusDays(15),
            LocalDateTime.now().minusDays(15)
        ),
        new User(
            5,
            "Emma Davis",
            "emma.davis@email.com",
            "+1-555-0105",
            List.of(NotificationCategory.FINANCIAL, NotificationCategory.SPORTS),
            List.of(NotificationType.EMAIL),
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(10)
        )
    );

    public List<User> findAll() {
        return users;
    }

}
