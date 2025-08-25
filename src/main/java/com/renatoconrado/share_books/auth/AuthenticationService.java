package com.renatoconrado.share_books.auth;

import com.renatoconrado.share_books.auth.dto.RegisterRequest;
import com.renatoconrado.share_books.email.EmailService;
import com.renatoconrado.share_books.email.EmailTemplate;
import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import com.renatoconrado.share_books.user.User;
import com.renatoconrado.share_books.user.UserRepository;
import com.renatoconrado.share_books.user.role.Role;
import com.renatoconrado.share_books.user.role.RoleRepository;
import com.renatoconrado.share_books.user.role.UserRole;
import com.renatoconrado.share_books.user.token.Token;
import com.renatoconrado.share_books.user.token.TokenRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${application.security.mail.frontend.activation-url}")
    private String activationUrl;

    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        Role role = this.roleRepository.findByName(DEFAULT_ROLE)
            .orElseThrow(() -> new EntityNotFoundException(DEFAULT_ROLE, Role.class));

        User user = User.builder()
            .username(request.getUsername())
            .email(request.getEmail())
            .password(this.passwordEncoder.encode(request.getPassword()))
            .realName(request.getRealName())
            .birthdate(request.getBirthdate())
            .isLocked(false)
            .isActive(false)
            .build();

        UserRole userRole = new UserRole(user, role);
        user.setUserRoles(Set.of(userRole));

        this.userRepository.save(user);
        this.sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token = this.generateAndSaveActivationToken(user);
        this.emailService.sendEmail(
            user.getEmail(),
            user.getUsername(),
            EmailTemplate.Name.ACTIVATE_ACCOUNT,
            this.activationUrl,
            token,
            "Account activation"
        );

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = this.generateActivationToken(6);
        var token = Token.builder()
            .content(generatedToken)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(30))
            .user(user)
            .build();
        this.tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationToken(int length) {
        var characters = "0123456789";
        var codeBuilder = new StringBuilder();
        var secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomI = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomI));
        }
        return codeBuilder.toString();
    }

}
