package org.bank.user.core.domain.account.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.core.domain.mail.AuthTemplateProperties;
import org.bank.user.core.domain.mail.TemporalPasswordTemplateProperties;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.repository.AccountMailStore;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthMailPublisher {

    private final AuthTemplateProperties authEmailTemplateProperties;
    private final TemporalPasswordTemplateProperties temporalPasswordEmailTemplateProperties;
    private final JavaMailSender mailSender;

    private final AccountMailStore redisAccountAuthMailRepository;

    public void sendVerificationAccountMailForCreate(Credential credential, Profile profile) {
        String token = redisAccountAuthMailRepository.save(VerificationReason.CREATE_ACCOUNT, credential, profile);
        sendMail(profile.getEmail(), authEmailTemplateProperties.generateTemplate(token));
    }

    public void sendVerificationAccountMailForFindID(List<Credential> credentials, String to) {
        String token = redisAccountAuthMailRepository.save(VerificationReason.FIND_ACCOUNT_ID, credentials);
        sendMail(to, authEmailTemplateProperties.generateTemplate(token));
    }

    public void sendVerificationAccountMailForUpdatePassword(Credential credential, String to) {
        String token = redisAccountAuthMailRepository.save(VerificationReason.FIND_PASSWORD, credential);
        sendMail(to, authEmailTemplateProperties.generateTemplate(token));
    }

    public void sendAccountMailForTemporalPassword(String to, String password) {
        sendMail(to, temporalPasswordEmailTemplateProperties.generateTemplate(password));
    }

    public void sendMail(String to, String template) {
        this.sendMail(new EmailSentEvent(to, template));
    }


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void sendMail(EmailSentEvent event) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setSubject("요청하신 이메일 인증을 안내해드립니다.");
            helper.setFrom("techfin@bank.com", "포트폴리오");
            helper.setTo(event.to());

            String emailContext = event.emailTemplate();
            helper.setText(emailContext, true);

        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
    }

}
