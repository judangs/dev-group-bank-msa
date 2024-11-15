package org.bank.user.core.user.application.usecase;

import org.bank.user.dto.AccountRequest;
import org.bank.user.dto.ResponseDto;
import org.bank.user.global.usecase.UseCase;

// 사용자 요구사항
public interface UserUseCase extends UseCase {

    boolean existsAccount(String userid);
    ResponseDto createAccount(AccountRequest request);
    ResponseDto withdrawAccount(String userid);
    ResponseDto editProfile(AccountRequest request, String userid);


    ResponseDto findAccountIDs(String username, String email);
    ResponseDto findAccountPassword(String userid, String email);

    ResponseDto confirmAccountEmail(String confirmParam);

}
