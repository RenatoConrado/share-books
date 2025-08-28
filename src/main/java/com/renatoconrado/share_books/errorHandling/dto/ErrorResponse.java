package com.renatoconrado.share_books.errorHandling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString
@Getter
public class ErrorResponse {

    private final Instant timestamp = Instant.now();
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, ?> details;

    public ErrorResponse(
        String message,
        HttpStatus status
    ) {
        this(message, status, Map.of());
    }

    public ErrorResponse(
        String message,
        HttpStatus status,
        Map<String, ?> details
    ) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.details = Map.copyOf(details);
    }
}
