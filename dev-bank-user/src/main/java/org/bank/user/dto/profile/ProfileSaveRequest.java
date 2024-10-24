package org.bank.user.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.bank.user.domain.profile.Address;

@Getter
public class ProfileSaveRequest {


    @NotNull
    private String name;
    @NotNull
    private String residentNumber;
    private Integer age;


    private Address address;

    @NotNull
    @Email
    private String email;
    private String phone;
}
