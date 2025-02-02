package org.bank.user.core.mail;

import lombok.RequiredArgsConstructor;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.mail.AccountMailFacade;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/mail")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final AccountMailFacade accountMailFacade;

    @GetMapping("/confirm-mail")
    public ResponseEntity<? super AccountIdListResponse> findAccountInfomation(@RequestParam String confirm) {
        ResponseDtoV2 response = accountMailFacade.confirmAccountEmail(confirm);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
