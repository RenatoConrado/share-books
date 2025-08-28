package com.renatoconrado.share_books.user;

import com.renatoconrado.share_books.errorHandling.exceptions.DuplicatedEntityException;
import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import com.renatoconrado.share_books.user.role.Role;
import com.renatoconrado.share_books.user.role.RoleService;
import com.renatoconrado.share_books.user.role.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UUID registerUser(User user) {
        Role role = this.roleService.findByName("USER");

        user.setUserRoles(Set.of(new UserRole(user, role)));

        return this.saveNewUser(user).getId();
    }

    /**
     * Validate the user and then save it
     * @return
     * @throws DuplicatedEntityException if fields {@link User#getUsername()}
     * and {@link User#getEmail()} already exists
     */
    public User saveNewUser(User user) throws DuplicatedEntityException {

        List<String> fields = new ArrayList<>();
        if (this.userRepository.existsByUsername(user.getUsername())) {
            fields.add("Username");
        }
        if (this.userRepository.existsByEmail(user.getEmail())) {
            fields.add("Email");
        }
        if (!fields.isEmpty()) {
            throw new DuplicatedEntityException(fields, User.class);
        }

        return this.userRepository.save(user);
    }

    /**
     * save without validate the user
     */
    public void savePersistedUser(User user) {
        this.userRepository.save(user);
    }

    /**
     * @throws EntityNotFoundException if User was Not found in the Database
     */
    public User findById(UUID id) throws EntityNotFoundException {
        return this.userRepository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(id, User.class)
        );
    }
}
