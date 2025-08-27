package com.renatoconrado.share_books.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationResponse(
    @NotBlank(message = "Field Cannot be empty")
    String token
) {}
