package com.renatoconrado.share_books.user.token;

import com.renatoconrado.share_books.errorHandling.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class ActivationCodeService {

    private final ActivationCodeRepository activationCodeRepository;

    public ActivationCode findByContent(String code) {
        return this.activationCodeRepository.findByContent(code)
            .orElseThrow(() -> new EntityNotFoundException(code, ActivationCode.class));
    }

    public void save(ActivationCode activationCode) {
        this.activationCodeRepository.save(activationCode);
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
