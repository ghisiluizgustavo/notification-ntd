package me.ghisiluizgustavo.notification.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response structure")
public record ErrorResponse(
    @Schema(description = "Timestamp when the error occurred", example = "2026-01-06T13:18:48.222912")
    LocalDateTime timestamp,
    
    @Schema(description = "HTTP status code", example = "400")
    int status,
    
    @Schema(description = "Error type", example = "Bad Request")
    String error,
    
    @Schema(description = "Detailed error message", example = "Invalid category 'INVALID'. Accepted values are: SPORTS, FINANCIAL, MOVIES")
    String message,
    
    @Schema(description = "Request path that caused the error", example = "/api/v1/notification")
    String path
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path
        );
    }
}
