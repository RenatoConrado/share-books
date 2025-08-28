package com.renatoconrado.share_books.errorHandling.exceptions;

public class ActivationCodeExpired extends RuntimeException {
    public ActivationCodeExpired(String message) {
        super("Activation token has expired. " + message);
    }
}
