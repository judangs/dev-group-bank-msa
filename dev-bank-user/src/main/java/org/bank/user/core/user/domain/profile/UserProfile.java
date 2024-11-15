package org.bank.user.core.user.domain.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.global.domain.base.DomainEntity;
import org.bank.user.core.user.domain.credential.UserCredential;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "profile")
public final class UserProfile extends DomainEntity {


    private String name;

    @Column(unique = true)
    private String residentNumber;
    private Integer age;


    // 알아보기.
    private Address address;
    private String email;
    private String phone;

    @Builder.Default
    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCredential> userCredentials = new ArrayList<>();


    // 어떤 사람이 사용하는 계정을 추가하는지
    public void createAccount(UserCredential userCredential) {


        // 해당 회원이 계정을 처음 만드는 상태인 경우
        if(userCredentials == null) {
            userCredentials = new ArrayList<>();
        }

        this.userCredentials.add(userCredential);
        userCredential.setUserProfile(this);

        // 계정 권한 생성(event)

        // access 도메인에 작업 실행 정보 생성(event)
        //userCredential.publish(new UserAuthEvent(
        //        DomainEvent.CREATED + DomainEvent.CREDENTIAL, DomainEvent.SUCCESS));


        // 메일 발송(event)

    }

    public void modifyProfile(UserProfile profile) {

        // Address, email, phone 변경
        this.address = profile.getAddress();
        this.email = profile.getEmail();
        this.phone = profile.getPhone();

        // 정보 변경 이벤트 발행

    }
}
