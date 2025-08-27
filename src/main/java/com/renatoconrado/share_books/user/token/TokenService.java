package com.renatoconrado.share_books.user.token;

import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token findByContent(String token) {
        return this.tokenRepository.findByContent(token)
            .orElseThrow(() -> new EntityNotFoundException("token", Token.class));
    }

    public void save(Token token) {
        this.tokenRepository.save(token);
    }

    public String generateActivationTokenContent(int length) {
        var characters = "0123456789";
        var tokenContent = new StringBuilder();
        var secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomI = secureRandom.nextInt(characters.length());
            tokenContent.append(characters.charAt(randomI));
        }
        return tokenContent.toString();
    }
}
