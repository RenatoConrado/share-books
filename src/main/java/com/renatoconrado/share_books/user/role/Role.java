package com.renatoconrado.share_books.user.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Table(catalog = "share-books", schema = "public", name = "roles")
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 20)
    @NotNull
    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserRole> userRoles = new LinkedHashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }
}
