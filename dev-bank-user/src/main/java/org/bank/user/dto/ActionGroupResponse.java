package org.bank.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bank.user.global.dto.ResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@NoArgsConstructor
public class ActionGroupResponse<T extends ResponseDto> {

    private final List<T> actions = new CopyOnWriteArrayList<>();


    public void nextAction(Class<T> actionClass, String code, String message) {

        T action = ResponseDto.from(actionClass)
                .code(code)
                .message(message)
                .completedAt(LocalDateTime.now())
                .build();


        this.actions.add(action);
    }
}
