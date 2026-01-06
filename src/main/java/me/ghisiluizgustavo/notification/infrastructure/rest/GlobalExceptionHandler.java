package me.ghisiluizgustavo.notification.infrastructure.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.ghisiluizgustavo.notification.domain.NotificationCategory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex,
        HttpServletRequest request
    ) {
        String message = ex.getMessage();
        
        if (message != null && message.contains("NotificationCategory")) {
            String detailedMessage = extractEnumErrorMessage(message);
            
            log.warn("Invalid category provided: {}", detailedMessage);
            
            ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                detailedMessage,
                request.getRequestURI()
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        log.error("Failed to read HTTP message: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            "Invalid request body. Please check the JSON format and field values.",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
        IllegalArgumentException ex,
        HttpServletRequest request
    ) {
        log.warn("Invalid argument: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Unexpected error occurred", ex);
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please try again later.",
            request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String extractEnumErrorMessage(String message) {
        if (message.contains("NotificationCategory") && message.contains("from String")) {
            String acceptedValues = Arrays.stream(NotificationCategory.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
            
            try {
                int startIdx = message.indexOf("\"") + 1;
                int endIdx = message.indexOf("\"", startIdx);
                String invalidValue = message.substring(startIdx, endIdx);
                
                return String.format(
                    "Invalid category '%s'. Accepted values are: %s",
                    invalidValue,
                    acceptedValues
                );
            } catch (Exception e) {
                return String.format("Invalid category. Accepted values are: %s", acceptedValues);
            }
        }
        
        return "Invalid request body format";
    }
}
