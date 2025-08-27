package com.renatoconrado.share_books.user.role;

import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return this.roleRepository.findByName(name)
            .orElseThrow(() -> new EntityNotFoundException(name, Role.class));
    }

}
