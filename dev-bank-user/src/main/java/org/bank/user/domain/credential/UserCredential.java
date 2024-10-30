package org.bank.user.domain.credential;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.domain.DomainEntity;
import org.bank.user.domain.DomainEvent;
import org.bank.user.domain.access.UserAuthEvent;
import org.bank.user.domain.access.UserCredentialAccess;
import org.bank.user.domain.policy.UserCredentialPolicy;
import org.bank.user.domain.profile.UserProfile;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "credential")
public class UserCredential extends DomainEntity {


    @Column(nullable = false, unique = true)
    private String userid;
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private RoleClassification.UserRole userType;

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

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void encryptPassword(String password) {
        this.password = password;
    }


    @Override
    protected final Optional<DomainEvent> includeOtherDomainField(DomainEvent event) {

        if(event instanceof UserAuthEvent userAuthEvent) {
            userAuthEvent.setUserid(userid);
            userAuthEvent.setUsername(username);
            return Optional.of(userAuthEvent);
        }

        return Optional.empty();

    }
}
