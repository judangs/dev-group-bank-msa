package org.bank.user.domain.credential;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.domain.access.UserCredentialAccess;
import org.bank.user.domain.policy.UserCredentialPolicy;
import org.bank.user.domain.profile.UserProfile;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userCredential")
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginID;
    private String password;
    private String nickname;
    private int userType;
    private String status;
    private String information;

    @ManyToOne
    @JoinColumn(name = "userProfileID")
    private UserProfile userProfile;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userCredentialID")
    private List<UserCredentialAccess> credentialAccess = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "userCredentialPolicyID")
    private UserCredentialPolicy userCredentialPolicy;

}
