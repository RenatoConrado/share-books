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
    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok().body("auth OK");
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        try {
            this.service.register(request);
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (MessagingException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@Valid @RequestBody String value) {
        return null;
    }

}
