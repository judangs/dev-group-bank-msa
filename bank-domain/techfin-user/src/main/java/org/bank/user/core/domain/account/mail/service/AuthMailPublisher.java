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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthMailPublisher {

    private final AuthTemplateProperties authEmailTemplateProperties;
    private final TemporalPasswordTemplateProperties temporalPasswordEmailTemplateProperties;
    private final JavaMailSender mailSender;

    private final AccountMailStore accountMailStore;

    // 무한 루프 발생할 수 있음. 여기서 어떻게 메시지 전송과 회원가입을 보장할 수 있는지?
    @Async
    public void sendVerificationAccountMailForCreate(Credential credential, Profile profile) {
        try {

            String token = accountMailStore.save(VerificationReason.CREATE_ACCOUNT, credential, profile);
            sendMail(profile.getEmail(), authEmailTemplateProperties.generateTemplate(token));

        }catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForCreate(credential, profile);
        }
    }

    @Async
    public void sendVerificationAccountMailForFindID(List<Credential> credentials, String to) {
        try {

            String token = accountMailStore.save(VerificationReason.FIND_ACCOUNT_ID, credentials);
            sendMail(to, authEmailTemplateProperties.generateTemplate(token));

        } catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForFindID(credentials, to);
        }
    }

    @Async
    public void sendVerificationAccountMailForUpdatePassword(Credential credential, String to) {
        try {
            String token = accountMailStore.save(VerificationReason.FIND_PASSWORD, credential);
            sendMail(to, authEmailTemplateProperties.generateTemplate(token));

        } catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForUpdatePassword(credential, to);
        }
    }

    @Async
    public void sendAccountMailForTemporalPassword(String to, String password) {
        sendMail(to, temporalPasswordEmailTemplateProperties.generateTemplate(password));
    }


    private void sendMail(String to, String emailTemplate) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setSubject("요청하신 이메일 인증을 안내해드립니다.");
            helper.setFrom("techfin@bank.com", "포트폴리오");
            helper.setTo(to);

            String emailContext = emailTemplate;
            helper.setText(emailContext, true);

        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
    }

}
