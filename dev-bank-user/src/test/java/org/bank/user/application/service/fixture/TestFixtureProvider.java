package org.bank.user.application.service.fixture;

import org.bank.user.domain.credential.RoleClassification;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.domain.profile.Address;
import org.bank.user.domain.profile.UserProfile;
import org.bank.user.domain.profile.UserProfileJpaRepository;
import org.bank.user.dto.AccountRequest;
import org.bank.user.dto.credential.CredentialSaveRequest;
import org.bank.user.dto.profile.ProfileSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;

@Component
public class TestFixtureProvider {

    @Autowired
    private UserProfileJpaRepository userProfileJpaRepository;
    @Autowired
    private UserCredentialJpaRepository userCredentialJpaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String createResidentNumber() {
        Random random = new Random();

        String prev = String.format("%05d", random.nextInt(10000));
        String next = String.format("%05d", random.nextInt(10000));
        return String.format("%s-%s", prev, next);
    }

    public UserProfile createProfileFixture(String username, String email) {

        UserProfile profile = UserProfile.builder()
                .name(username)
                .residentNumber(createResidentNumber())
                .age(26)
                .address(new Address("도시", "구/군", "도로명 주소", "우편 번호", "상세 주소"))
                .email(email)
                .phone("010-1234-1234")
                .build();

        userProfileJpaRepository.save(profile);
        return profile;
    }

    public UserCredential createCredentialFixture(String username, String userid, String password) {

        Optional<UserProfile> profile = Optional.ofNullable(userProfileJpaRepository.findByName(username)
                .orElseGet(() -> createProfileFixture(username, "fixture@email.com")));


        UserCredential credential = UserCredential.builder()
                .userid(userid)
                .password(passwordEncoder.encode(password))
                .username(username)
                .userProfile(profile.get())
                .build();

        userCredentialJpaRepository.save(credential);
        return credential;
    }

    public AccountRequest createAccountRequestFixture(String username, String userid, String password, RoleClassification.UserRole role) {
        ProfileSaveRequest profile = ProfileSaveRequest.builder()
                .name(username)
                .residentNumber(createResidentNumber())
                .age(26)
                .email("fixture-email@bank.com")
                .build();

        CredentialSaveRequest credential = CredentialSaveRequest.builder()
                .userid(userid)
                .password(passwordEncoder.encode(password))
                .role(role)
                .username(username)
                .build();

        return new AccountRequest(profile, credential);
    }




}
