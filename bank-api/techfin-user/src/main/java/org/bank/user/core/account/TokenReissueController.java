package org.bank.user.core.account;

import lombok.RequiredArgsConstructor;
import org.bank.core.dto.response.ResponseCodeV2;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountAuthenticationFacade;
import org.bank.user.dto.service.response.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/auth")
public class TokenReissueController {

    private final UserAccountAuthenticationFacade userAccountAuthenticationFacade;

    @PostMapping("/expire-token")
    public String reissue(@RequestBody String token) {

        ResponseDtoV2 response = userAccountAuthenticationFacade.reissue(token);
        if(response.getCode().equals(ResponseCodeV2.SUCCESS)) {
            if(response instanceof LoginResponse reissueResponse) {
                return reissueResponse.getToken().getAccess();
            }
        }

        throw new RuntimeException("토큰을 발행하는데 실패했습니다.");
    }

}
