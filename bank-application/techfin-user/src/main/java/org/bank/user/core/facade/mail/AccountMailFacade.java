package org.bank.user.core.facade.mail;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthenticationException;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.domain.mail.AccountVerificationMail;
import org.bank.user.core.domain.mail.VerificationReason;
import org.bank.user.core.domain.mail.service.AccountAuthMailService;
import org.bank.user.core.producer.registration.AccountRegistrationEventPublisher;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountMailFacade {

    private final AccountAuthMailService accountAuthMailService;
    private final AccountRegistrationEventPublisher accountRegistrationEventPublisher;

    public ResponseDtoV2 confirmAccountEmail(String token) throws AuthenticationException {

        VerificationReason reason = accountAuthMailService.getEmailSendingReason(token);

        ResponseDtoV2 responseBody = switch(reason) {
            case CREATE_ACCOUNT -> {
                AccountVerificationMail verify = accountAuthMailService.verifyAccountEmailForCreation(token);
                accountRegistrationEventPublisher.userCreated(verify);
                yield ResponseDtoV2.success("사용자 계정 생성에 성공했습니다.");
            }
            case FIND_ACCOUNT_ID -> {
                List<String> useridList = accountAuthMailService.verifyAccountEmailForID(token);
                yield AccountIdListResponse.builder()
                        .code(ResponseCodeV2.SUCCESS)
                        .message("사용자 계정의 아이디 조회에 성공했습니다.")
                        .userid(useridList)
                        .completedAt(LocalDateTime.now())
                        .build();
            }
            case FIND_PASSWORD -> {
                accountAuthMailService.verifyAccountEmailForFindPassword(token);
                yield ResponseDtoV2.success("임시 패스워드 메일을 발송했습니다.");
            }

            default -> ResponseDtoV2.fail("잘못된 요청입니다.");
        };

        accountAuthMailService.completeVerificationProcess(token);
        return responseBody;
    }
}
