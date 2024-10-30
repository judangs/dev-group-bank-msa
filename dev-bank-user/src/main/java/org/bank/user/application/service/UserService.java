package org.bank.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.user.application.usecase.UserUseCase;
import org.bank.user.domain.credential.RoleClassification;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.infra.jpa.UserCredentialJpaRepository;
import org.bank.user.domain.profile.UserProfile;
import org.bank.user.domain.profile.UserProfileJpaRepository;
import org.bank.user.dto.ActionGroupResponse;
import org.bank.user.dto.CreateAccountRequest;
import org.bank.user.dto.CreateAccountResponse;

import org.bank.user.exception.credential.PermissionException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserProfileJpaRepository userProfileJpaRepository;
    private final UserCredentialJpaRepository userCredentialJpaRepository;

    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ActionGroupResponse createAccount(CreateAccountRequest request) {

        // 동일한 ID로 생성된 계정이 존재하는지 확인하기
        Optional<UserCredential> existCredential = userCredentialJpaRepository.findByUserid(request.getCredential().getUserid());
        if(existCredential.isPresent()) {
            throw new PermissionException();
        }

        // 사용자 역할이 생성 가능한 역할인지 확인, 은행을 이용하는 Customer 역할이 아니라면 계정을 생성할 수 없음
        RoleClassification role = RoleClassification.of(request.getCredential().getRole());
        if(!RoleClassification.CUSTOMER.equals(role)) {

            throw new PermissionException();
        }


        // 고객이 이전에 방문한 고객인지 확인
        UserProfile userProfile = userProfileJpaRepository.findByNameAndResidentNumber(
                    request.getProfile().getName(), request.getProfile().getResidentNumber()
                )
                .orElseGet(() -> (UserProfile) request.toDomain(UserProfile.class).get());


        UserCredential credential = (UserCredential) request.toDomain(UserCredential.class).get();
        String encrypt =  passwordEncoder.encode(credential.getPassword());
        credential.encryptPassword(encrypt);

        userProfile.createAccount(credential);
        userProfileJpaRepository.save(userProfile);



        ActionGroupResponse response = new ActionGroupResponse();
        response.nextAction(CreateAccountResponse.class, 200, "계정 생성이 정상적으로 완료되었습니다.");

        return response;
    }



}
