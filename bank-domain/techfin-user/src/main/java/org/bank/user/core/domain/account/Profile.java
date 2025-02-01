package org.bank.user.core.domain.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.core.common.Address;
import org.bank.core.common.Role;
import org.bank.user.global.domain.DomainEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
@Table(name = "user_profile_tb")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public final class Profile extends DomainEntity {

    @Id
    @Column(name = "profile_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String residentNumber;
    private Integer age;

    @Embedded
    private Address address;

    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role.UserRole role;

    @Builder.Default
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credential> credentials = new ArrayList<>();


    public void create(Credential credential) {
        if(credentials == null) {
            credentials = new ArrayList<>();
        }

        if(role == null) role = Role.UserRole.INDIVIDUAL;

        this.credentials.add(credential);
        credential.setProfile(this);
    }

    public void initializeProfileWithResidentNumber(Credential credential, String residentNumber) {
        create(credential);
        this.residentNumber = residentNumber;
    }

    public void replace(Profile profile) {
        if(Objects.nonNull(profile.getAddress())) this.address = profile.getAddress();
        if(Objects.nonNull(profile.getEmail())) this.email = profile.getEmail();
        if(Objects.nonNull(profile.getPhone())) this.phone = profile.getPhone();
    }
}
