package org.bank.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActionGroupResponse<T extends ActionResponse> {

    private List<T> actions = new ArrayList<>();

    public void nextAction(T action) {
        this.actions.add(action);
    }
}
