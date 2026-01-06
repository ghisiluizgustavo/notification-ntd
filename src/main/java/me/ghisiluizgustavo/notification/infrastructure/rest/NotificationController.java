package me.ghisiluizgustavo.notification.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import me.ghisiluizgustavo.notification.feature.listhistory.ListNotificationHistoryHandler;
import me.ghisiluizgustavo.notification.feature.listhistory.NotificationHistoryResponse;
import me.ghisiluizgustavo.notification.feature.notifyusers.NotificationRequest;
import me.ghisiluizgustavo.notification.feature.notifyusers.NotifyUsersHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotifyUsersHandler notifyUsersHandler;
    private final ListNotificationHistoryHandler listNotificationHistoryHandler;

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void notifyUsers(@RequestBody NotificationRequest notificationRequest) {
        notifyUsersHandler.handle(notificationRequest);
    }

    @GetMapping("/logs")
    public List<NotificationHistoryResponse> getHistory() {
        return listNotificationHistoryHandler.handle();
    }

}
