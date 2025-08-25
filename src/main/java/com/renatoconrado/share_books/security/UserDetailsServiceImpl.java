package com.renatoconrado.share_books.security;

import com.renatoconrado.share_books.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        return this.userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Details not Found"));
    }
}
