package org.bank.user.dto;

import lombok.Getter;
import org.bank.user.domain.DomainEntity;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.profile.UserProfile;
import org.bank.user.dto.credential.CredentialSaveRequest;
import org.bank.user.dto.profile.ProfileSaveRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Getter
public class CreateAccountRequest extends ActionRequest {

    private ProfileSaveRequest profile;
    private CredentialSaveRequest credential;

    public CreateAccountRequest() {
        this.createAt = LocalDateTime.now();
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
