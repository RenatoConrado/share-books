package com.renatoconrado.share_books.errorHandling.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object searchParam, Class<?> entity) {
        super(String.format(
            "%s based in %s search param was not found",
            entity.getSimpleName(), searchParam
        ));
    }
}
