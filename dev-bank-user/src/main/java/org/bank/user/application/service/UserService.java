package org.bank.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.bank.user.application.provider.MailProvider;
import org.bank.user.application.usecase.UserUseCase;
import org.bank.user.common.response.ResponseCode;
import org.bank.user.domain.credential.RoleClassification;
import org.bank.user.domain.credential.UserCredential;
import org.bank.user.domain.credential.repository.jpa.UserCredentialJpaRepository;
import org.bank.user.domain.profile.UserProfile;
import org.bank.user.domain.profile.UserProfileJpaRepository;
import org.bank.user.dto.AccountRequest;
import org.bank.user.dto.AccountResponse;
import org.bank.user.dto.ResponseDto;
import org.bank.user.exception.credential.InvalidArgumentException;
import org.bank.user.exception.credential.PermissionException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserProfileJpaRepository userProfileJpaRepository;
    private final UserCredentialJpaRepository userCredentialJpaRepository;

    private final MailProvider mailProvider;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean existsAccount(String userid) {

        Optional<UserCredential> existCredential = userCredentialJpaRepository.findByUserid(userid);
        if(existCredential.isPresent()) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void confirmAccountEmail(String confirmParam) {

        UserCredential userCredential = mailProvider.confirmAccountMail(confirmParam);
        userCredentialJpaRepository.save(userCredential);
    }


    @Override
    public ResponseDto createAccount(AccountRequest request) {

        // 사용자 계정 아이디가 중복인지 다시 확인
        if(existsAccount(request.getCredential().getUserid())) {
            throw new InvalidArgumentException("사용자 아이디를 사용할 수 없습니다.");
        }

        // 사용자 역할이 생성 가능한 역할인지 확인, 은행을 이용하는 Customer 역할이 아니라면 계정을 생성할 수 없음
        if(request.getCredential().getRole() == null) {
            request.getCredential().defaultRole();
        }
        RoleClassification role = RoleClassification.of(request.getCredential().getRole());
        if(!RoleClassification.CUSTOMER.equals(role)) {

            throw new PermissionException();
        }


        // 고객이 이전에 방문한 고객인지 확인
        UserProfile userProfile = userProfileJpaRepository.findByNameAndResidentNumber(
                    request.getProfile().getName(), request.getProfile().getResidentNumber()
                )
                .orElseGet(() -> (UserProfile) request.toDomain(UserProfile.class).get());
        userProfileJpaRepository.save(userProfile);


        UserCredential credential = (UserCredential) request.toDomain(UserCredential.class).get();
        String encrypt =  passwordEncoder.encode(credential.getPassword());
        credential.encryptPassword(encrypt);

        userProfile.createAccount(credential);

        mailProvider.sendVerificationAccounttMail(credential, userProfile.getEmail());


        return ResponseDto.from(AccountResponse.class)
                .code(ResponseCode.SUCCESS)
                .message("인증 메일이 전송되었습니다.")
                .completedAt(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto editProfile(AccountRequest request, String userid) {


        Optional<UserCredential> existCredential = userCredentialJpaRepository.findByUserid(userid);
        if(existCredential.isEmpty()) {
            throw new InvalidArgumentException("로그인 중인 사용자만 정보 변경이 가능합니다.");
        }

        UserProfile profile = existCredential.get().getUserProfile();
        profile.modifyProfile((UserProfile) request.toDomain(UserProfile.class).get());


        // 이벤트 발행
        return ResponseDto.from(AccountResponse.class)
                .code(ResponseCode.SUCCESS)
                .message("사용자 정보 변경이 완료되었습니다.")
                .completedAt(LocalDateTime.now())
                .build();
    }


    @Override
    @Transactional
    public ResponseDto withdrawAccount(String userid) {

        Optional<UserCredential> credential = userCredentialJpaRepository.findByUserid(userid);
        if(credential.isEmpty()) {
            throw new InvalidArgumentException("잘못된 접근입니다.");
        }

        credential.get().withdrawAccount();

        return ResponseDto.from(AccountResponse.class)
                .code(ResponseCode.SUCCESS)
                .message("사용자 계정 탈퇴가 완료되었습니다.")
                .completedAt(LocalDateTime.now())
                .build();
    }


}
