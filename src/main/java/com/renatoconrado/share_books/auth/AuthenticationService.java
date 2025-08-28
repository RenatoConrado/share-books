package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.AuthenticationRequest;
import com.renatoconrado.share_books.auth.dto.AuthenticationResponse;
import com.renatoconrado.share_books.auth.dto.RegisterRequest;
import com.renatoconrado.share_books.email.EmailService;
import com.renatoconrado.share_books.email.EmailTemplate;
import com.renatoconrado.share_books.errorHandling.exceptions.TokenExpiredException;
import com.renatoconrado.share_books.security.jwt.JwtService;
import com.renatoconrado.share_books.user.User;
import com.renatoconrado.share_books.user.UserService;
import com.renatoconrado.share_books.user.token.Token;
import com.renatoconrado.share_books.user.token.TokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.security.mail.frontend.activation-url}")
    private String activationUrl;

    public void register(@Valid RegisterRequest request) throws MessagingException {
        User user = User.builder()
            .username(request.username())
            .email(request.email())
            .password(this.passwordEncoder.encode(request.password()))
            .realName(request.realName())
            .birthdate(request.birthdate())
            .isLocked(false)
            .isActive(false)
            .build();

        this.userService.registerUser(user);
        this.sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String tokenContent = this.generateAndSaveActivationToken(user);
        this.emailService.sendEmail(
            user.getEmail(),
            user.getUsername(),
            EmailTemplate.Name.ACTIVATE_ACCOUNT,
            this.activationUrl,
            tokenContent,
            "Account activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String tokenContent = this.tokenService.generateActivationTokenContent(6);
        Token token = Token.newActivationToken(tokenContent, user);
        this.tokenService.save(token);
        return tokenContent;
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request) {
        var authentication = this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
            )
        );
        var user = (User) authentication.getPrincipal();

        String jwtToken;
        if (user.getRealName() != null) {
            Map<String, Object> claims = Map.of("fullName", user.getRealName());

            jwtToken = this.jwtService.generateToken(claims, user);
            return new AuthenticationResponse(jwtToken);
        }

        jwtToken = this.jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public void activateAccount(String token) throws MessagingException {
        Token savedToken = this.tokenService.findByContent(token);

        if (savedToken.hasExpired()) {
            this.sendValidationEmail(savedToken.getUser());
            throw new TokenExpiredException("A new Token has been sent.");
        }

        User user = this.userService.findById(savedToken.getUser().getId());
        user.setIsActive(true);
        this.userService.savePersistedUser(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        this.tokenService.save(savedToken);
    }
}
