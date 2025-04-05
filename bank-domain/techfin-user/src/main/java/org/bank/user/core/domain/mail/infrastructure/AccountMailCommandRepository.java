package org.bank.user.core.domain.mail.infrastructure;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.repository.AccountMailReader;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

@Profile(value = {"develop"})
@Repository
@RequiredArgsConstructor
public class AccountMailCommandRepository implements AccountMailReader {

    private final AccountMailRepository accountMailRepository;

    @Override
    public AccountVerificationMail findById(String id) throws NoSuchElementException {
        return accountMailRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public VerificationReason findVerificationReasonById(String id) throws NoSuchElementException, TimeoutException {
        AccountVerificationMail verificationMail = accountMailRepository.findById(id).orElseThrow(NoSuchElementException::new);
        if(verificationMail.isExpired()) {
            throw new TimeoutException();
        }

        verificationMail.confirm();
        return verificationMail.getReason();
    }

    @Override
    public List<String> findVerifierUserIdById(String id) {
        AccountVerificationMail verificationMail = accountMailRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return verificationMail.getVerifierInfos().stream()
                .map(AccountVerificationMail.VerifierInfo::getUserid).toList();
    }
}
