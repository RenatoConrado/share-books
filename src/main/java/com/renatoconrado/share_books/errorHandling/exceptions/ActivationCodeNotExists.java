package com.renatoconrado.share_books.errorHandling.exceptions;

public class ActivationCodeNotExists extends RuntimeException {
    public ActivationCodeNotExists(String message) {
        super("Activation token dont exists. " + message);
    }
}
