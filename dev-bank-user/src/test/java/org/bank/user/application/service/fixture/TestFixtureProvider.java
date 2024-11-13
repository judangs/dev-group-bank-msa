package org.bank.user.application.service.fixture;

import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.domain.profile.UserProfile;
import org.bank.user.domain.profile.UserProfileJpaRepository;
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
                .email(email)
                .build();

        userProfileJpaRepository.save(profile);
        return profile;
    }

    public UserCredential createCredentialFixture(String username, String userid, String password) {

        Optional<UserProfile> profile = Optional.ofNullable(userProfileJpaRepository.findByName(username)
                .orElseGet(() -> createProfileFixture(username, "dummy@email.com")));



        UserCredential credential = UserCredential.builder()
                .userid(userid)
                .password(passwordEncoder.encode(password))
                .username(username)
                .userProfile(profile.get())
                .build();

        userCredentialJpaRepository.save(credential);
        return credential;
    }




}
