package org.bank.user.core.domain.mail.service;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthenticationException;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.mail.service.AuthMailPublisher;
import org.bank.user.core.domain.account.service.CredentialService;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.repository.AccountMailReader;
import org.bank.user.core.domain.mail.repository.AccountMailStore;
import org.bank.user.core.domain.account.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class AccountAuthMailService {

    private final CredentialService credentialService;
    private final AuthMailPublisher authMailPublisher;

    private final AccountMailStore accountMailStore;
    private final AccountMailReader accountMailReader;

    public VerificationReason getEmailSendingReason(String token) {
        try {
            return accountMailReader.findVerificationReasonById(token);
        } catch (TimeoutException e) {
            accountMailStore.deleteById(token);
            throw new AuthenticationException("인증 제한 시간이 초과되었습니다.");
        }
    }

    public void verifyAccountEmailForCreation(String token) {
        try {
            AccountVerificationMail mail = accountMailReader.findById(token);
            credentialService.save(mail.info());
        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.");
        }
    }

    public List<String> verifyAccountEmailForID(String token) {
        try {
            return accountMailReader.findVerifierUserIdById(token);
        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.");
        }
    }

    public void verifyAccountEmailForFindPassword(String token) {
        try {
            AccountVerificationMail mail = accountMailReader.findById(token);
            Credential credential = mail.info();
            String temporalPassword = credentialService.createTemporalPassword(mail.info());

            Profile profile = credential.getProfile();
            authMailPublisher.sendAccountMailForTemporalPassword(profile.getEmail(), temporalPassword);

        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.");
        }
    }

    public void completeVerificationProcess(String token) {
        accountMailStore.deleteById(token);
    }
}
