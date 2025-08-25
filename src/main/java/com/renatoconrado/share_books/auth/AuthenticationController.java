package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public ResponseEntity<Object> post(@Valid @RequestBody RegisterRequest request)
        throws MessagingException {
        this.service.register(request);
        return ResponseEntity.accepted().build();
    }

}
