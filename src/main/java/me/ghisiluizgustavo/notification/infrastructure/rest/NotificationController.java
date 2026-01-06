package me.ghisiluizgustavo.notification.infrastructure.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Notifications", description = "API for sending and retrieving notifications")
public class NotificationController {

    private final NotifyUsersHandler notifyUsersHandler;
    private final ListNotificationHistoryHandler listNotificationHistoryHandler;

    @Operation(
        summary = "Send notification to subscribed users",
        description = "Sends a notification to all users subscribed to the specified category through their registered channels (EMAIL, SMS, PUSH)"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notification sent successfully"),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid request - validation error or invalid category",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void notifyUsers(@Valid @RequestBody NotificationRequest notificationRequest) {
        notifyUsersHandler.handle(notificationRequest);
    }

    @Operation(
        summary = "Get notification logs",
        description = "Retrieves all notification records sorted from newest to oldest"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved notification logs",
            content = @Content(schema = @Schema(implementation = NotificationHistoryResponse.class))
        )
    })
    @GetMapping("/logs")
    public List<NotificationHistoryResponse> getHistory() {
        return listNotificationHistoryHandler.handle();
    }

}
