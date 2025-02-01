package org.bank.user.core.domain.fixture;

import org.bank.core.common.Address;
import org.bank.core.common.Role;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AccountFixture {

    public static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static final String password = "password";

    public static Credential authenticated(String userid) {
        return Credential.builder()
                .userid(userid)
                .username("username")
                .password(passwordEncoder.encode(password))
                .build();
    }

    public static Credential credential(String userid) {
        return Credential.builder()
                .userid(userid)
                .username("username")
                .password("password")
                .build();
    }

    public static Profile profile(String name, String residentNumber, String email) {
        return Profile.builder()
                .name(name).email(email)
                .residentNumber(residentNumber)
                .address(new Address())
                .build();
    }

    public static Profile admin(String name, String residentNumber, String email) {
        return Profile.builder()
                .name(name).email(email)
                .residentNumber(residentNumber)
                .role(Role.UserRole.ADMIN)
                .address(new Address())
                .build();
    }
}
