package org.bank.user.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.application.provider.PasswordProvider;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCredentialService {

    private final UserCredentialJpaRepository userCredentialJpaRepository;

    private final PasswordProvider passwordProvider;
    private final PasswordEncoder passwordEncoder;


    public String createTemporalPassword(UserCredential userCredential) {

        String temporalPassword = passwordProvider.generate();
        userCredential.encryptPassword(passwordEncoder.encode(temporalPassword));
        save(userCredential);

        return temporalPassword;
    }

    public void save(UserCredential userCredential) {
        userCredentialJpaRepository.save(userCredential);
    }


}
