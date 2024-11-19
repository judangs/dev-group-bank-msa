package org.bank.user.core.mail.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.user.core.mail.application.usecase.AccountMailUseCase;
import org.bank.user.core.mail.domain.MailCache;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingCommandRepository;
import org.bank.user.core.mail.domain.repository.MailVerificationPendingQueryRepository;
import org.bank.user.core.user.application.provider.MailProvider;
import org.bank.user.core.user.application.service.UserCredentialService;
import org.bank.user.core.user.domain.credential.UserCredential;
import org.bank.user.core.user.domain.profile.UserProfile;
import org.bank.user.dto.AccountResponse;
import org.bank.user.global.dto.ResponseDto;
import org.bank.user.global.mail.CacheType;
import org.bank.user.global.response.ResponseCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


@Component
@RequiredArgsConstructor
public class AccountMailService implements AccountMailUseCase {

    private final MailProvider mailProvider;
    private final MailVerificationPendingCommandRepository mailPendingCommandRepository;

    private final UserCredentialService userCredentialService;
    private final MailVerificationPendingQueryRepository mailPendingQueryRepository;


    @Override
    @Transactional
    public ResponseDto confirmAccountEmail(String token) {

        ResponseDto responseBody;
        try {

            CacheType cacheType = mailPendingCommandRepository.findForCacheTypeById(token);
            responseBody = switch(cacheType) {
                case CREATE_ACCOUNT -> {
                    verifyAccountEmailForCreation(token);
                    yield ResponseDto.success("사용자 계정 생성에 성공했습니다.");

                }
                case FIND_ACCOUNT_ID -> {
                    List<String> useridList = verifyAccountEmailForID(token);
                    yield AccountResponse.builder()
                            .code(ResponseCode.SUCCESS)
                            .message("사용자 계정의 아이디 조회에 성공했습니다.")
                            .userid(useridList)
                            .completedAt(LocalDateTime.now())
                            .build();
                }
                case FIND_PASSWORD -> {
                    verifyAccountEmailForFindPassword(token);
                    yield ResponseDto.success("임시 패스워드 메일을 발송했습니다.");
                }

                default -> ResponseDto.fail("잘못된 요청입니다.");
            };

            mailPendingQueryRepository.deleteById(token);
        } catch (NoSuchElementException e) {
            // 부적절한 token이 전달된 경우
            throw new AuthenticationException("잘못된 요청입니다..") {};
        } catch (TimeoutException e) {
            mailPendingQueryRepository.deleteById(token);
            throw new AuthenticationException("인증 제한 시간이 초과되었습니다.") {};
        }

        return responseBody;
    }



    private void verifyAccountEmailForCreation(String token) {

        try {
            MailCache mailCache = mailPendingCommandRepository.findById(token);
            userCredentialService.save(mailCache.getFrontCredential());
        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.") {};
        }
    }

    private List<String> verifyAccountEmailForID(String token) {

        List<String> useridList;
        try {
            useridList = mailPendingCommandRepository.findById(token)
                    .getCredentials().stream()
                    .map(UserCredential::getUserid)
                    .toList();

        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.") {};
        }

        return useridList;
    }

    private void verifyAccountEmailForFindPassword(String token) {

        try {

            MailCache mailCache = mailPendingCommandRepository.findById(token);

            UserCredential credential = mailCache.getFrontCredential();
            String temporalPassword = userCredentialService.createTemporalPassword(mailCache.getFrontCredential());

            UserProfile profile = credential.getUserProfile();
            mailProvider.sendAccountMailForTemporalPassword(profile.getEmail(), temporalPassword);


        } catch (NoSuchElementException e) {
            throw new AuthenticationException("이메일 인증에 실패했습니다.") {};
        }
    }
}
