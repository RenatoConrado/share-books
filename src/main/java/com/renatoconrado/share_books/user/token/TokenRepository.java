package com.renatoconrado.share_books.user.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository
    extends JpaRepository<Token, UUID>, JpaSpecificationExecutor<Token> {
    Optional<Token> findByContent(String content);
}
