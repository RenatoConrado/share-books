package com.renatoconrado.share_books.errorHandling.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class DuplicatedEntityException extends RuntimeException {

    private final List<String> duplicatedFields;
    private final Class<?> entity;

    public DuplicatedEntityException(List<String> duplicatedFields, Class<?> entity) {
        super(String.format(
            "Duplicate detected in %s for fields: %s",
            entity.getSimpleName(),
            String.join(", ", duplicatedFields)
        ));
        this.duplicatedFields = duplicatedFields;
        this.entity = entity;
    }
}
