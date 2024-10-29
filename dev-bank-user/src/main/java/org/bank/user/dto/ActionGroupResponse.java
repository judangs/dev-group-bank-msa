package org.bank.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ActionGroupResponse<T extends ActionResponse> {

    private final List<T> actions = new ArrayList<>();

    public void nextAction(Class<T> actionClass, Integer status, String message) {

        T action = ActionResponse.from(actionClass)
                .status(status)
                .message(message)
                .actionClass(actionClass.getName())
                .completedAt(LocalDateTime.now())
                .build();


        this.actions.add(action);
    }
}
