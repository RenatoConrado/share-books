package com.renatoconrado.share_books.errorHandling;

import com.renatoconrado.share_books.errorHandling.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public static ResponseEntity<ErrorResponse> buildErrorResponse(
        String message,
        HttpStatus status
    ) {
        return buildErrorResponse(message, status, Map.of());
    }

    public static ResponseEntity<ErrorResponse> buildErrorResponse(
        String message,
        HttpStatus status,
        Map<String, ?> extraPairs
    ) {
        var errorDetails = new ErrorResponse(
            message,
            status,
            extraPairs
        );

        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
        log.error("Unhandled error ", ex);
        return buildErrorResponse(
            "Unexpected internal error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR,
            Map.of(
                "message",
                "Unexpected error occurred. Please contact support if the issue persists"
            )
        );
    }

}
