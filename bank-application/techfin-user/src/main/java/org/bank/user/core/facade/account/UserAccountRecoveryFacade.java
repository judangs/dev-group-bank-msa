package org.bank.user.core.facade.account;

import lombok.RequiredArgsConstructor;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.domain.account.service.AccountRecoveryService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountRecoveryFacade {

    private final AccountRecoveryService accountRecoveryService;

    public ResponseDtoV2 findAccountID(String username, String email) {

        ResponseCodeV2 code = accountRecoveryService.findAccountIDs(username, email);
        return switch (code)  {
            case SUCCESS -> ResponseDtoV2.success("인증 메일이 전송되었습니다.");
            case NOT_FOUND -> ResponseDtoV2.of(ResponseCodeV2.NOT_FOUND, "등록된 사용자가 없습니다.");
            default -> ResponseDtoV2.fail("부적절한 요청입니다. 다시 시도해주세요.");
        };
    };

    public ResponseDtoV2 findAccountPassword(String userid, String email) {
        ResponseCodeV2 code = accountRecoveryService.findAccountPassword(userid, email);
        return switch (code)  {
            case SUCCESS -> ResponseDtoV2.success("인증 메일이 전송되었습니다.");
            case NOT_FOUND -> ResponseDtoV2.of(ResponseCodeV2.NOT_FOUND, "등록된 사용자가 없습니다.");
            default -> ResponseDtoV2.fail("부적절한 요청입니다. 다시 시도해주세요.");
        };
    }


}
