package com.renatoconrado.share_books.errorHandling;

import com.renatoconrado.share_books.errorHandling.exceptions.DuplicatedEntityException;
import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicatedEntityException(
        DuplicatedEntityException ex
    ) {
        Map<String, Object> error = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.CONFLICT.value(),
            "error", HttpStatus.CONFLICT.getReasonPhrase(),
            "entity", ex.getEntity().getSimpleName(),
            "duplicatedFields", ex.getDuplicatedFields(),
            "message", ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(
        EntityNotFoundException ex
    ) {
        Map<String, Object> error = Map.of(
            "timestamp", LocalDateTime.now(),
            "status", HttpStatus.NOT_FOUND.value(),
            "error", HttpStatus.NOT_FOUND.getReasonPhrase(),
            "entity", ex.getEntity().getSimpleName(),
            "searchParam", ex.getSearchParam(),
            "message", ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
