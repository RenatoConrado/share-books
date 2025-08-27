package com.renatoconrado.share_books.errorHandling.exceptions;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final Object searchParam;
    private final Class<?> entity;

    public EntityNotFoundException(Object searchParam, Class<?> entity) {
        super(String.format(
            "Entity: %s, based in query: %s was not found",
            entity.getSimpleName(), searchParam
        ));
        this.searchParam = searchParam;
        this.entity = entity;
    }
}
