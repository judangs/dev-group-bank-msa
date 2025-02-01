package org.bank.user.core.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.common.Role;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.repository.CredentialRepository;
import org.bank.user.core.domain.crypto.PasswordProvider;
import org.bank.user.core.domain.account.mail.service.AuthMailPublisher;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.account.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements AccountManagerService, AccountRecoveryService {

    private final ProfileRepository profileRepository;
    private final CredentialRepository credentialRepository;

    private final PasswordProvider passwordProvider;
    private final AuthMailPublisher authMailPublisher;

    @Override
    public boolean existsAccount(String userid) {

        Optional<Credential> existCredential = credentialRepository.findByUserid(userid);
        if(existCredential.isPresent()) {
            return true;
        }

        return false;
    }


    @Override
    public ResponseCodeV2 createAccount(Profile profile, Credential credential) {

        if(existsAccount(credential.getUserid())) {
            return ResponseCodeV2.FAIL;
        }

        if(Objects.nonNull(profile.getRole()) && Role.TELLER.equals(Role.of(profile.getRole()))) {
            return ResponseCodeV2.FORBIDDEN;
        }

        profileRepository.save(profile);

        passwordProvider.encode(credential);
        profile.create(credential);
        authMailPublisher.sendVerificationAccountMailForCreate(credential, profile);

        return ResponseCodeV2.SUCCESS;
    }

    @Override
    @Transactional
    public ResponseCodeV2 editProfile(Profile profile, AuthClaims user) {

        Optional<Credential> optionalCredential = credentialRepository.findByUserid(user.getUserid());
        if(optionalCredential.isEmpty())
            return ResponseCodeV2.UNAUTHORIZED;

        optionalCredential.map(Credential::getProfile)
                .ifPresent(userProfile -> userProfile.replace(profile));
        return ResponseCodeV2.SUCCESS;
    }

    @Override
    @Transactional
    public ResponseCodeV2 withdrawAccount(AuthClaims user){

        Optional<Credential> optionalCredential = credentialRepository.findByUserid(user.getUserid());
        if(optionalCredential.isEmpty())
            return ResponseCodeV2.UNAUTHORIZED;

        optionalCredential.ifPresent(Credential::withdraw);
        return ResponseCodeV2.SUCCESS;
    }

    @Override
    public ResponseCodeV2 findAccountIDs(String username, String email) {

        Optional<Profile> existProfile = profileRepository.findByNameAndEmail(username, email);
        if(existProfile.isEmpty())
            return ResponseCodeV2.NOT_FOUND;


        existProfile.map(Profile::getCredentials)
                .ifPresent(credentials -> authMailPublisher.sendVerificationAccountMailForFindID(credentials, email));
        return ResponseCodeV2.SUCCESS;
    }

    @Override
    public ResponseCodeV2 findAccountPassword(String userid, String email) {

        Optional<Credential> existCredential = credentialRepository.findByUserid(userid);
        if(existCredential.isEmpty())
            return ResponseCodeV2.NOT_FOUND;

        existCredential.ifPresent(credential -> authMailPublisher.sendVerificationAccountMailForUpdatePassword(credential, email));
        return ResponseCodeV2.SUCCESS;
    }
}

