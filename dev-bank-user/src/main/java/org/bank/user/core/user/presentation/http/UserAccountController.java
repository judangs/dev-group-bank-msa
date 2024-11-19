package org.bank.user.core.user.presentation.http;

import lombok.RequiredArgsConstructor;
import org.bank.user.core.user.application.usecase.UserUseCase;
import org.bank.user.dto.AccountResponse;
import org.bank.user.global.dto.ResponseDto;
import org.bank.user.dto.AccountRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/user/account")
@RestController
public class UserAccountController {

    private final UserUseCase userUseCase;

    @PostMapping("/create")
    public ResponseEntity<? super AccountResponse> create(@RequestBody AccountRequest request) {

        ResponseDto responseBody = userUseCase.createAccount(request);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/find-id")
    public ResponseEntity<? super AccountResponse> findAccountUserid(
            @RequestParam String username,
            @RequestParam String email
    ) {

        ResponseDto responseBody = userUseCase.findAccountIDs(username, email);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping("/find-password")
    public ResponseEntity<? super AccountResponse> updateAccountUserid(
            @RequestParam("userid") String userid,
            @RequestParam("email") String email
    ) {
        ResponseDto responseBody = userUseCase.findAccountPassword(userid, email);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }



}
