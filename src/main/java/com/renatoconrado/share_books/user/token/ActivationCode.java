package com.renatoconrado.share_books.user.token;

import com.renatoconrado.share_books.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Table(catalog = "share-books", schema = "public", name = "activation_codes")
@Entity
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 255)
    @NotBlank(message = "Field Cannot be empty")
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static ActivationCode newActivationToken(String content, User user) {
        return ActivationCode.builder()
            .content(content)
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusMinutes(30))
            .user(user)
            .build();
    }

    public boolean hasExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
