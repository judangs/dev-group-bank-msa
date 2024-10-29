package org.bank.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Getter
@SuperBuilder(toBuilder = true)
public abstract class ActionResponse {

    protected Integer status;
    protected String message;
    protected String actionClass;

    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime completedAt;


    @SuppressWarnings("unchecked")
    public static <R extends  ActionResponse> ActionResponseBuilder<R, ?> from(Class<R> actionClass) {
        try {
            Class<?> builderClass = Class.forName(actionClass.getName() + "Builder");
            return (ActionResponseBuilder<R, ?>) builderClass.getDeclaredMethod("buidler").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
