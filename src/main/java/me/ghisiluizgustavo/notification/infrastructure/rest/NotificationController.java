package me.ghisiluizgustavo.notification.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import me.ghisiluizgustavo.notification.feature.notifyusers.NotifyUsersHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotifyUsersHandler notifyUsersHandler;

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void notifyUsers(@RequestBody NotificationRequest notificationRequest) {
        notifyUsersHandler.handle(notificationRequest);
    }

    public record NotificationRequest(
        NotificationCategory category,
        String content
    ) {
    }
}
