package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.AuthenticationRequest;
import com.renatoconrado.share_books.auth.dto.AuthenticationResponse;
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
    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok().body("auth OK");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request)
        throws MessagingException {
        this.service.register(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(this.service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<String> activateAccount(@RequestParam String token)
        throws MessagingException {
        this.service.activateAccount(token);
        return ResponseEntity.ok().build();
    }

}
