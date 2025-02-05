package org.bank.user.core.domain.mail;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class AccountVerificationMail {

    private VerificationReason reason;
    private List<VerifierInfo> verifierInfos = new ArrayList<>();
    private LocalDateTime expire;
    private boolean cacheRead = false;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VerifierInfo {

        private String userid;
        private String password;
        private String username;

        private String email;
        private String residentNumber;

        public static VerifierInfo of(Credential credential) {

            return VerifierInfo.builder()
                    .userid(credential.getUserid())
                    .password(credential.getPassword())
                    .username(credential.getUsername())
                    .residentNumber(credential.getProfile().getResidentNumber())
                    .email(credential.getProfile().getEmail())
                    .build();
        }


        public static VerifierInfo of(Credential credential, Profile profile) {
            return VerifierInfo.builder()
                    .userid(credential.getUserid())
                    .password(credential.getPassword())
                    .username(credential.getUsername())
                    .email(profile.getEmail())
                    .residentNumber(profile.getResidentNumber())
                    .build();
        }

        public static Credential to(VerifierInfo verifierInfo) {
            Credential credential = Credential.builder()
                    .userid(verifierInfo.getUserid())
                    .password(verifierInfo.getPassword())
                    .username(verifierInfo.getUsername())
                    .build();

            if(Objects.nonNull(verifierInfo.getEmail()) && Objects.nonNull(verifierInfo.getResidentNumber()))
                credential.getProfile().initializeUniqueProfile(credential, verifierInfo.getResidentNumber(), verifierInfo.getEmail());

            return credential;
        }
    }

    public AccountVerificationMail(VerificationReason reason, Credential credential) {
        this.reason = reason;
        this.verifierInfos.add(VerifierInfo.of(credential));
        this.expire = LocalDateTime.now().plusMinutes(3);
    }

    public AccountVerificationMail(VerificationReason reason, List<Credential> credentials) {
        this.reason = reason;
        this.verifierInfos = credentials.stream()
                .map(VerifierInfo::of).toList();
        this.expire = LocalDateTime.now().plusMinutes(3);
    }

    public AccountVerificationMail(VerificationReason reason, Credential credential, Profile profile) {
        this.reason = reason;
        this.verifierInfos.add(VerifierInfo.of(credential, profile));
        this.expire = LocalDateTime.now().plusMinutes(3);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expire);
    }

    public void confirm() {
        this.cacheRead = true;
    }


    public Credential info() {
        if(verifierInfos.isEmpty())
            throw new NoSuchElementException();

        return VerifierInfo.to(verifierInfos.get(0));
    }

}
