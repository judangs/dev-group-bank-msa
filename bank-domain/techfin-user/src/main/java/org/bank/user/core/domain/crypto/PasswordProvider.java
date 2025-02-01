package org.bank.user.core.domain.crypto;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.account.Credential;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class PasswordProvider {

    private final PasswordEncoder passwordEncoder;

    private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
    private final int PASSWORD_LENGTH = 12;

    public boolean matches(String rawPassword, String encryptPassword) {
        return passwordEncoder.matches(rawPassword, encryptPassword);
    }

    public void encode(Credential credential) {
        String encryptPassword = passwordEncoder.encode(credential.getPassword());
        credential.setPassword(encryptPassword);
    }

    public String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return passwordEncoder.encode(password.toString());
    }

    public void generate(Credential credential) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        String encryptPassword = passwordEncoder.encode(password.toString());
        credential.setPassword(encryptPassword);
    }

}