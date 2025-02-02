package org.bank.user.core.account;

import lombok.RequiredArgsConstructor;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountRecoveryFacade;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class AccountRecoveryController {

    private final UserAccountRecoveryFacade userAccountRecoveryFacade;

    @PostMapping("/find-id")
    public ResponseEntity<? super AccountIdListResponse> findAccountUserid(
            @RequestParam String username,
            @RequestParam String email
    ) {

        ResponseDtoV2 response = userAccountRecoveryFacade.findAccountID(username, email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/find-password")
    public ResponseEntity<? super AccountIdListResponse> findAccountUserPassword(
            @RequestParam("userid") String userid,
            @RequestParam("email") String email
    ) {
        ResponseDtoV2 response = userAccountRecoveryFacade.findAccountPassword(userid, email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
