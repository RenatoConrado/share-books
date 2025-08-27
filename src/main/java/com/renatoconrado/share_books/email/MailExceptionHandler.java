package com.renatoconrado.share_books.email;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static com.renatoconrado.share_books.errorHandling.GlobalExceptionHandler.buildErrorResponse;

@RestControllerAdvice
public class MailExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<Map<String, Object>> handleMessagingException(
        MessagingException ex
    ) {
        return buildErrorResponse(
            "Failed to send email: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<Map<String, Object>> handleMailException(MailException ex) {
        return buildErrorResponse(
            "Mail server error: " + ex.getMessage(),
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

}
