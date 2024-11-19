package org.bank.user.dto;

import lombok.Getter;
import org.bank.user.global.domain.base.DomainEntity;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.profile.UserProfile;
import org.bank.user.dto.credential.CredentialSaveRequest;
import org.bank.user.dto.profile.ProfileSaveRequest;
import org.bank.user.global.dto.requestDto;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public final class AccountRequest extends requestDto {

    private ProfileSaveRequest profile;
    private CredentialSaveRequest credential;

    public AccountRequest() {

        this.createAt = LocalDateTime.now();
    }

    public AccountRequest(final ProfileSaveRequest profile, final CredentialSaveRequest credential) {
        this();
        this.profile = profile;
        this.credential = credential;
    }

    @Override
    public Optional<DomainEntity> toDomain(Class<?> domainClass) {

        if(domainClass.equals(UserProfile.class)) {
            return Optional.of(toUserProfileDomain());
        }
        if(domainClass.equals(UserCredential.class)) {
            return Optional.of(toUserCredentialDomain());
        }

        return Optional.empty();
    }

    private UserProfile toUserProfileDomain() {
        return UserProfile.builder()
                .name(profile.getName())
                .residentNumber(profile.getResidentNumber())
                .age(profile.getAge())
                .phone(profile.getPhone())
                .email(profile.getEmail())
                .address(profile.getAddress())
                .build();
    }

    private UserCredential toUserCredentialDomain() {
        return UserCredential.builder()
                .userid(credential.getUserid())
                .password(credential.getPassword())
                .username(credential.getUsername())
                .userProfile(toUserProfileDomain())
                .build();
    }
}
