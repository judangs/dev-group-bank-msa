package org.bank.user.core.user.application.provider;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.bank.user.global.property.EmailProperties;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.credential.repository.MailVerificationPendingQueue;
import org.bank.user.global.exception.InvalidArgumentException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailProvider {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;
    private final MailVerificationPendingQueue mailPendingQueue;


    @Async
    public void sendVerificationAccounttMail(UserCredential credential, String to) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String verificationLink = UUID.randomUUID().toString();

        try {
            helper.setSubject("회원가입 서비스를 위한 이메일 인증");
            helper.setFrom("portfolio@bank.com", "은행 포트폴리오");
            helper.setTo(to);

            String emailContent = emailProperties.getTemplate(verificationLink);
            helper.setText(emailContent, true);


        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
        mailPendingQueue.save(verificationLink, credential);
    }


    public UserCredential confirmAccountMail(String confirmUri) {
        if(!mailPendingQueue.existsById(confirmUri)) {
            // 시간 초과로 다시 이메일 인증을 해야 함.
            throw new InvalidArgumentException();
        }

        UserCredential credential = mailPendingQueue.findById(confirmUri);
        mailPendingQueue.deleteById(confirmUri);
        return credential;
    }
}
