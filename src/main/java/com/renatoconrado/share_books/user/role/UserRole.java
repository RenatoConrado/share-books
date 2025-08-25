package com.renatoconrado.share_books.user.role;

import com.renatoconrado.share_books.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(catalog = "share-books", schema = "public", name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @LastModifiedDate
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        this.id = new UserRoleId(user.getId(), role.getId());
        log.info(
            "userRoleId: {}, userId: {}, roleId: {}",
            this.id, user.getId(), role.getId()
        );
    }

    public UserRole() {}
}
