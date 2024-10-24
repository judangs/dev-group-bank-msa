package org.bank.user.dto.profile;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.user.dto.ActionResponse;


@Getter
@SuperBuilder
public class ProfileSaveResponse extends ActionResponse {

    private String data;

}
