package org.bank.user.domain.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.domain.credential.UserCredential;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userProfile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String residentNumber;
    private Integer age;

    // convert 클래스 확인해보기.
    private String address;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "userProfile")
    private List<UserCredential> userCredential;

}
