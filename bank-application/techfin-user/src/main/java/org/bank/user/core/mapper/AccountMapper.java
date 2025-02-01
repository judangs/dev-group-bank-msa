package org.bank.user.core.mapper;

import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.account.service.AccountDomainMapper;
import org.bank.user.dto.service.request.AccountRequest;
import org.bank.user.dto.service.request.AccountRequest.CredentialRequest;
import org.bank.user.dto.service.request.AccountRequest.ProfileRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountMapper implements AccountDomainMapper<AccountRequest> {

    @Override
    public Profile toProfile(AccountRequest request) {

        ProfileRequest profileRequest = request.getProfile();

        return Profile.builder()
                .name(profileRequest.getName())
                .residentNumber(profileRequest.getResidentNumber())
                .age(profileRequest.getAge())
                .phone(profileRequest.getPhone())
                .email(profileRequest.getEmail())
                .address(profileRequest.getAddress())
                .build();
    }

    @Override
    public Credential toCredential(AccountRequest request) {

        CredentialRequest credentialRequest = request.getCredential();

        return Credential.builder()
                .userid(credentialRequest.getUserid())
                .password(credentialRequest.getPassword())
                .username(credentialRequest.getUsername())
                .build();
    }
}
