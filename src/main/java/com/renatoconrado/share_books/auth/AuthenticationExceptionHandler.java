package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.errorHandling.dto.ErrorResponse;
import com.renatoconrado.share_books.errorHandling.exceptions.TokenExpiredException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static com.renatoconrado.share_books.errorHandling.GlobalExceptionHandler.buildErrorResponse;

@Slf4j
@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpired(TokenExpiredException ex) {
        return buildErrorResponse(
            "Token has expired",
            HttpStatus.FORBIDDEN,
            Map.of("message", ex.getMessage())
        );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleUserLocked() {
        return buildErrorResponse(
            "Account is locked",
            HttpStatus.FORBIDDEN,
            Map.of("message", "Valid credentials, but account is locked")
        );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledUser() {
        return buildErrorResponse(
            "Account is disabled",
            HttpStatus.FORBIDDEN,
            Map.of("message", "Valid credentials, but account is disabled")
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException() {
        return buildErrorResponse(
            "Invalid credentials",
            HttpStatus.UNAUTHORIZED,
            Map.of("message", "Account is enabled and unlocked, but password is invalid.")
        );
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<ErrorResponse> handleDecodingException(
        DecodingException decode
    ) {
        log.error("error decoding JWT token key", decode);
        return buildErrorResponse(
            "Internal configuration error",
            HttpStatus.INTERNAL_SERVER_ERROR,
            Map.of("message", "Invalid or malformed authentication token")
        );
    }

    @ExceptionHandler(WeakKeyException.class)
    public ResponseEntity<ErrorResponse> handleWeakKeyException(
        WeakKeyException weakKey
    ) {
        log.error("JWT Sign in key is less than 256 bits (32 bytes)", weakKey);
        return buildErrorResponse(
            "Internal configuration error",
            HttpStatus.INTERNAL_SERVER_ERROR,
            Map.of("message", "Weak JWT signing key")
        );
    }
}
