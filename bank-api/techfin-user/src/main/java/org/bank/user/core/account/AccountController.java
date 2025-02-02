package org.bank.user.core.account;


import lombok.RequiredArgsConstructor;
import org.bank.core.auth.AuthClaims;
import org.bank.core.auth.AuthenticatedUser;
import org.bank.core.dto.response.ResponseDtoV2;
import org.bank.user.core.facade.account.UserAccountManagementFacade;
import org.bank.user.dto.service.request.AccountRequest;
import org.bank.user.dto.service.response.AccountIdListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/account")
@RequiredArgsConstructor
public class AccountController {

    private final UserAccountManagementFacade userAccountManagementFacade;

    @PostMapping("/create")
    public ResponseEntity<? super AccountIdListResponse> create(@RequestBody AccountRequest accountRequest) {

        ResponseDtoV2 response = userAccountManagementFacade.create(accountRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/edit")
    public ResponseEntity<? extends ResponseDtoV2> edit(
            @RequestBody AccountRequest accountRequest,
            @AuthenticatedUser AuthClaims user
    ) {

        ResponseDtoV2 response = userAccountManagementFacade.edit(accountRequest, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<? extends ResponseDtoV2> withdraw(
            @AuthenticatedUser AuthClaims user
    ) {

        ResponseDtoV2 response = userAccountManagementFacade.withdraw(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

