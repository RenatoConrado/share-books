package com.renatoconrado.share_books.errorHandling.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super("Activation token has expired. " + message);
    }
}
