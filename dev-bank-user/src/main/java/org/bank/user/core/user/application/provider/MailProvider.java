package org.bank.user.core.user.application.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.MailVerificationPendingQueue;
import org.bank.user.global.mail.CacheType;
import org.bank.user.global.property.EmailProperties;
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

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final MailVerificationPendingQueue mailPendingQueue;


    @Async
    public void sendVerificationAccountMailForCreate(UserCredential credential, String to) {
        String PendingQueueKey = sendMail(to);
        mailPendingQueue.save(PendingQueueKey, CacheType.CREATE_ACCOUNT, Arrays.asList(credential));
    }

    @Async
    public void sendVerificationAccountMailForFindID(List<UserCredential> credentials, String to) {
        String pendingQueueKey = sendMail(to);
        mailPendingQueue.save(pendingQueueKey, CacheType.FIND_ACCOUNT_ID, credentials);
    }

    @Async
    public void sendVerificationAccountMailForUpdatePassword(UserCredential credential, String to) {
        String pendingQueueKey = sendMail(to);
        mailPendingQueue.save(pendingQueueKey, CacheType.CHANGE_PASSWORD, Arrays.asList(credential));
    }

    public void confirmAccountEmail(String validParam) {
        mailPendingQueue.deleteById(validParam);
    }


    private String sendMail(String to) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String verificationLink = UUID.randomUUID().toString();

        try {
            helper.setSubject("요청하신 이메일 인증을 안내해드립니다.");
            helper.setFrom("portfolio@bank.com", "은행 포트폴리오");
            helper.setTo(to);

            String emailContent = emailProperties.getTemplate(verificationLink);
            helper.setText(emailContent, true);


        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
        return verificationLink;
    }

}
