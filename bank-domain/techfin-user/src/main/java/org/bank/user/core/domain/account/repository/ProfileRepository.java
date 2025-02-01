package org.bank.user.core.domain.account.repository;

import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;

import java.util.Optional;

public interface ProfileRepository {

    void save(Profile profile);
    void save(Credential credential);

    Optional<Profile> findByNameAndEmail(String name, String email);
    Optional<Profile> findByName(String username);
    Optional<Profile> findByNameAndResidentNumber(String name, String residentNumber);
}