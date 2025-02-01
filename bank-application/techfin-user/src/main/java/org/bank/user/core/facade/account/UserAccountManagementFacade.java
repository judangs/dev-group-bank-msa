package org.bank.user.core.facade.account;

import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.mapper.AccountMapper;
import org.bank.user.core.domain.account.Credential;
import org.bank.user.core.domain.account.service.AccountManagerService;
import org.bank.user.core.domain.account.Profile;
import org.bank.user.dto.service.request.AccountRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountManagementFacade {

    private final AccountManagerService accountManagerService;
    private final AccountMapper accountMapper;

    public ResponseDtoV2 create(AccountRequest accountRequest) {

        Profile profile = accountMapper.toProfile(accountRequest);
        Credential credential = accountMapper.toCredential(accountRequest);

        ResponseCodeV2 code = accountManagerService.createAccount(profile, credential);
        return switch (code)  {
            case SUCCESS -> {
                yield ResponseDtoV2.success("인증 메일이 전송되었습니다.");
            }
            case FORBIDDEN -> ResponseDtoV2.forbidden("해당 작업은 관리자에게 문의하세요.");
            default -> ResponseDtoV2.fail("부적절한 요청입니다. 다시 시도해주세요.");
        };
    };

    public ResponseDtoV2 edit(AccountRequest request, AuthClaims user) {

        Profile profile = accountMapper.toProfile(request);

        ResponseCodeV2 code = accountManagerService.editProfile(profile, user);
        return switch (code)  {
            case SUCCESS -> {
                yield ResponseDtoV2.success("사용자 변경이 완료되었습니다.");
            }
            case UNAUTHORIZED -> ResponseDtoV2.unauthorized("로그인 중인 사용자만 이용이 가능합니다.");
            default -> ResponseDtoV2.fail("부적절한 요청입니다. 다시 시도해주세요.");
        };
    }

    public ResponseDtoV2 withdraw(AuthClaims user) {
        ResponseCodeV2 code = accountManagerService.withdrawAccount(user);
        return switch (code)  {
            case SUCCESS -> {
                yield ResponseDtoV2.success("사용자 계정 탈퇴가 완료되었습니다.");
            }
            case UNAUTHORIZED -> ResponseDtoV2.unauthorized("로그인 중인 사용자만 이용이 가능합니다.");
            default -> ResponseDtoV2.fail("부적절한 요청입니다. 다시 시도해주세요.");
        };
    }
}
