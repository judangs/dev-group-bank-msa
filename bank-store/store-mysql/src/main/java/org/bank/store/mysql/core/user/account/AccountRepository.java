package org.bank.store.mysql.core.user.account;


import lombok.RequiredArgsConstructor;
import org.bank.store.mysql.core.user.account.jpa.CredentialJpaRepository;
import org.bank.store.mysql.core.user.account.jpa.ProfileJpaRepository;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.account.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository implements ProfileRepository, CredentialRepository {

    private final CredentialJpaRepository credentialJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;

    @Override
    public Optional<Credential> findByUserid(String userid) {
        return credentialJpaRepository.findByUserid(userid);
    }

    @Override
    public Optional<Credential> findByUsername(String username) {
        return credentialJpaRepository.findByUsername(username);
    }

    @Override
    public void save(Profile profile) {
        profileJpaRepository.save(profile);
    }

    @Override
    public void save(Credential credential) {

        Profile profile = credential.getProfile();

        profileJpaRepository.findProfileByResidentNumberAndEmail(profile.getResidentNumber(), profile.getEmail())
                .ifPresent(userProfile -> {
                    userProfile.getCredentials()
                            .removeIf((existingCredential) -> existingCredential.equals(credential));

                    userProfile.create(credential);
                    profileJpaRepository.save(userProfile);
                });
    }

    @Override
    public Optional<Profile> findByNameAndEmail(String name, String email) {
        return profileJpaRepository.findByNameAndEmail(name, email);
    }

    @Override
    public Optional<Profile> findByName(String username) {
        return profileJpaRepository.findByName(username);
    }

    @Override
    public Optional<Profile> findByNameAndResidentNumber(String name, String residentNumber) {
        return profileJpaRepository.findByNameAndResidentNumber(name, residentNumber);
    }
}
