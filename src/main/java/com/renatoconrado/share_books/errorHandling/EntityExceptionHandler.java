package com.renatoconrado.share_books.errorHandling;

import com.renatoconrado.share_books.errorHandling.dto.ErrorResponse;
import com.renatoconrado.share_books.errorHandling.exceptions.DuplicatedEntityException;
import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.renatoconrado.share_books.errorHandling.GlobalExceptionHandler.buildErrorResponse;

@Slf4j
@RestControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(DuplicatedEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedEntityException(
        DuplicatedEntityException ex
    ) {
        return buildErrorResponse(
            "This Entity has duplicated fields",
            HttpStatus.CONFLICT,
            Map.of(
                "message", ex.getMessage(),
                "entity", ex.getEntity().getSimpleName(),
                "duplicatedFields", ex.getDuplicatedFields()
            )
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
        EntityNotFoundException ex
    ) {
        return buildErrorResponse(
            "Entity was not found",
            HttpStatus.NOT_FOUND,
            Map.of(
                "message", ex.getMessage(),
                "entity", ex.getEntity().getSimpleName(),
                "value", ex.getSearchParam()
            )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
        MethodArgumentNotValidException ex
    ) {
        log.warn("Validation Error: {}", ex.getMessage());

        List<Map<String, String>> errors = ex.getFieldErrors()
            .stream()
            .map(fe -> Map.of(
                "message", Objects.toString(fe.getDefaultMessage()),
                "field", fe.getField(),
                "value", Objects.toString(fe.getRejectedValue())
            ))
            .toList();

        return buildErrorResponse(
            "Validation failed",
            HttpStatus.BAD_REQUEST,
            Map.of(
                "message", "Invalid Fields",
                "invalidFields", errors
            )
        );
    }

}
