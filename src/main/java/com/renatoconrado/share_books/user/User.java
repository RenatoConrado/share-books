package com.renatoconrado.share_books.user;

import com.renatoconrado.share_books.user.role.Role;
import com.renatoconrado.share_books.user.role.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "share-books", schema = "public", name = "users")
@Entity
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 70, message = "Max size of 70 characters")
    @NotNull(message = "Field Cannot be null")
    @Column(name = "username", nullable = false, length = 70, unique = true)
    private String username;

    @Email(message = "Email invalid")
    @Size(max = 255, message = "Max size of 255 characters")
    @NotNull(message = "Field Cannot be null")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(min = 8, max = 255, message = "minimum size of 8 characters")
    @NotNull(message = "Field Cannot be null")
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 70, message = "Max size of 70 characters")
    @Column(name = "real_name", length = 70)
    private String realName;

    @Past(message = "Date must be in the past")
    @Column(name = "birthdate")
    private LocalDateTime birthdate;

    @PastOrPresent(message = "Date must be in the past or present")
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PastOrPresent(message = "Date must be in the past or present")
    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @NotNull(message = "Field Cannot be null")
    @ColumnDefault("false")
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;

    @NotNull(message = "Field Cannot be null")
    @ColumnDefault("false")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.userRoles
            .stream()
            .map(userRole -> (GrantedAuthority)
                () -> "ROLE_" + userRole.getRole().getName()
            )
            .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }
}
