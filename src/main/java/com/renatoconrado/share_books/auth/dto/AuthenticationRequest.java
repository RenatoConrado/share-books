package com.renatoconrado.share_books.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
    @Email(message = "Email invalid")
    @Size(max = 255, message = "Max size of 255 characters")
    @NotBlank(message = "Field Cannot be empty")
    String email,

    @Size(min = 8, max = 255, message = "minimum size of 8 characters")
    @NotBlank(message = "Field Cannot be empty")
    String password,

    @Size(max = 70, message = "Max size of 70 characters")
    String realName
) {}
