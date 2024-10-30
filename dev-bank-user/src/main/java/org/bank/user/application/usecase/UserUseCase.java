package org.bank.user.application.usecase;

import org.bank.user.dto.ActionGroupResponse;
import org.bank.user.dto.ActionResponse;
import org.bank.user.dto.CreateAccountRequest;
import org.bank.user.dto.credential.CredentialSaveRequest;
import org.bank.user.dto.profile.ProfileSaveRequest;

public interface UserUseCase extends UseCase {

    // 사용자의 요구사항은 계정 생성
    ActionGroupResponse createAccount(CreateAccountRequest request);
    // 계정 삭제
    //ActionGroupResponse deleteAccount(DeleteAccountRequest request);
    // 계정 수정
    //ActionGroupResponse modifyAccount(ModifyAccountRequest request);


}
