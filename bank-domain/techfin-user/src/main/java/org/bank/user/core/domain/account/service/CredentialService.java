package org.bank.user.core.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.repository.ProfileRepository;
import org.bank.user.core.domain.crypto.PasswordProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final PasswordProvider passwordProvider;

    private final ProfileRepository profileRepository;

    public String createTemporalPassword(Credential credential) {
        String password = passwordProvider.generate(credential);
        save(credential);
        return password;
    }

    public void save(Credential credential) {
        profileRepository.save(credential);
    }
}
