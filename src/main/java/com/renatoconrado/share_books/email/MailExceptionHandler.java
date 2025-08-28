package com.renatoconrado.share_books.email;

import com.renatoconrado.share_books.errorHandling.dto.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> handleMessagingException(
        MessagingException ex
    ) {
        return buildErrorResponse(
            "Failed to send email",
            HttpStatus.INTERNAL_SERVER_ERROR,
            Map.of("message", ex.getMessage())
        );
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleMailException(MailException ex) {
        return buildErrorResponse(
            "Mail server error",
            HttpStatus.SERVICE_UNAVAILABLE,
            Map.of("message", ex.getMessage())
        );
    }

}
