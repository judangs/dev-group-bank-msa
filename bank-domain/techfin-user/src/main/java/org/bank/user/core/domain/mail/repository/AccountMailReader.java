package org.bank.user.core.domain.mail.repository;

import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;

public interface AccountMailReader {
    VerificationReason findVerificationReasonById(String id) throws TimeoutException;
    AccountVerificationMail findById(String id) throws NoSuchElementException;
    List<String> findVerifierUserIdById(String id);
}
