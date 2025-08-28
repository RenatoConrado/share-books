package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.AuthenticationRequest;
import com.renatoconrado.share_books.auth.dto.AuthenticationResponse;
import com.renatoconrado.share_books.auth.dto.RegisterRequest;
import com.renatoconrado.share_books.email.EmailService;
import com.renatoconrado.share_books.email.EmailTemplate;
import com.renatoconrado.share_books.errorHandling.exceptions.ActivationCodeExpired;
import com.renatoconrado.share_books.errorHandling.exceptions.ActivationCodeNotExists;
import com.renatoconrado.share_books.security.jwt.JwtService;
import com.renatoconrado.share_books.user.User;
import com.renatoconrado.share_books.user.UserRepository;
import com.renatoconrado.share_books.user.UserService;
import com.renatoconrado.share_books.user.token.ActivationCode;
import com.renatoconrado.share_books.user.token.ActivationCodeService;
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
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivationCodeService codeService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.security.mail.frontend.activation-url}")
    private String activationUrl;

    public UUID register(@Valid RegisterRequest request) throws MessagingException {
        User user = User.builder()
            .username(request.username())
            .email(request.email())
            .password(this.passwordEncoder.encode(request.password()))
            .realName(request.realName())
            .birthdate(request.birthdate())
            .isLocked(false)
            .isActive(false)
            .build();

        UUID id = this.userService.registerUser(user);
        this.sendValidationEmail(user);
        return id;
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
        String tokenContent = this.codeService.generateActivationTokenContent(6);
        ActivationCode activationCode = ActivationCode.newActivationToken(
            tokenContent,
            user
        );
        this.codeService.save(activationCode);
        return tokenContent;
    }

    public void activateAccount(String code) throws MessagingException {
        ActivationCode savedActivationCode = this.codeService.findByContent(code);

        if (savedActivationCode.hasExpired()) {
            this.sendValidationEmail(savedActivationCode.getUser());
            throw new ActivationCodeExpired(
                "A new activation code has been sent to your email"
            );
        }

        User user = this.userService.findById(savedActivationCode.getUser().getId());
        user.setIsActive(true);
        this.userService.savePersistedUser(user);

        savedActivationCode.setValidatedAt(LocalDateTime.now());
        this.codeService.save(savedActivationCode);
    }

    public AuthenticationResponse authenticate(@Valid AuthenticationRequest request)
    throws MessagingException {
        this.sendEmailIfUserNotHaveActivationCode(request.email());

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

    private void sendEmailIfUserNotHaveActivationCode(String email)
    throws MessagingException {
        var optUser = this.userRepository.findByEmail(email);
        if (optUser.isEmpty()) {
            return;
        }
        var user = optUser.get();

        if (user.getActivationCodes().isEmpty()) {
            this.sendValidationEmail(user);
            throw new ActivationCodeNotExists(
                "A new activation code has been sent to your email"
            );
        }
    }
}
