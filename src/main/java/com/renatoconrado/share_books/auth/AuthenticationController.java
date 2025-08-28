package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.AuthenticationRequest;
import com.renatoconrado.share_books.auth.dto.AuthenticationResponse;
import com.renatoconrado.share_books.auth.dto.RegisterRequest;
import com.renatoconrado.share_books.util.UriUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("auth OK");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request)
    throws MessagingException {
        UUID id = this.service.register(request);

        return ResponseEntity.created(UriUtil.buildUri("/users/{id}", id)).build();
    }

    @GetMapping("/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam String code)
    throws MessagingException {
        this.service.activateAccount(code);
        return ResponseEntity.ok("Account successfully activated");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @Valid @RequestBody AuthenticationRequest request
    ) throws MessagingException {
        return ResponseEntity.ok(this.service.authenticate(request));
    }

}
