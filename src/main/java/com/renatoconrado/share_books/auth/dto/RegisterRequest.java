package com.renatoconrado.share_books.auth.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
public class RegisterRequest {

    @Size(max = 70, message = "Max size of 70 characters")
    @NotNull(message = "Field Cannot be null")
    private String username;

    @Email(message = "Email invalid")
    @Size(max = 255, message = "Max size of 255 characters")
    @NotNull(message = "Field Cannot be null")
    private String email;

    @Size(min = 8, max = 255, message = "minimum size of 8 characters")
    @NotNull(message = "Field Cannot be null")
    private String password;

    @Size(max = 70, message = "Max size of 70 characters")
    private String realName;

    @Past(message = "Date must be in the past")
    private LocalDateTime birthdate;
}
