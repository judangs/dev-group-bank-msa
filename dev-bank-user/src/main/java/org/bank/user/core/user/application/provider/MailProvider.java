package org.bank.user.core.user.application.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingQueryRepository;
import org.bank.user.global.mail.CacheType;
import org.bank.user.global.property.EmailProperties;
import org.bank.user.global.provider.KeyProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailProvider {

    private final EmailProperties emailProperties;

    private final JavaMailSender mailSender;

    private final MailVerificationPendingQueryRepository mailPendingQueryRepository;

    public String generate(KeyProvider keyProvider) {
        return keyProvider.createKey();
    }


    @Async
    public void sendVerificationAccountMailForCreate(UserCredential credential, String to) {
        try {

            String token = generate(() -> UUID.randomUUID().toString());
            sendMail(to, emailProperties.getVerticationMailTemplate(token));
            mailPendingQueryRepository.save(token, CacheType.CREATE_ACCOUNT, Arrays.asList(credential));

        }catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForCreate(credential, to);
        }
    }

    @Async
    public void sendVerificationAccountMailForFindID(List<UserCredential> credentials, String to) {
        try {

            String token = generate(() -> UUID.randomUUID().toString());
            sendMail(to, emailProperties.getVerticationMailTemplate(token));
            mailPendingQueryRepository.save(token, CacheType.FIND_ACCOUNT_ID, credentials);

        } catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForFindID(credentials, to);
        }
    }

    @Async
    public void sendVerificationAccountMailForUpdatePassword(UserCredential credential, String to) {
        try {

            String token = generate(() -> UUID.randomUUID().toString());
            sendMail(to, emailProperties.getVerticationMailTemplate(token));
            mailPendingQueryRepository.save(token, CacheType.FIND_PASSWORD, Arrays.asList(credential));

        } catch (DataIntegrityViolationException e) {
            this.sendVerificationAccountMailForUpdatePassword(credential, to);
        }
    }

    @Async
    public void sendAccountMailForTemporalPassword(String to, String password) {
        sendMail(to, emailProperties.getTemporalPasswordTemplate(password));
    }


    private void sendMail(String to, String emailTemplate) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            helper.setSubject("요청하신 이메일 인증을 안내해드립니다.");
            helper.setFrom("portfolio@bank.com", "은행 포트폴리오");
            helper.setTo(to);

            String emailContext = emailTemplate;
            helper.setText(emailContext, true);


        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
    }

}
