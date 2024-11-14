package org.bank.user.dto.profile;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bank.user.dto.ResponseDto;


@Getter
@SuperBuilder
public class ProfileSaveResponseDto extends ResponseDto {

    private String data;

}
