package org.bank.consumer.global.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventTypeCaster {

    public <R extends KafkaEvent> R cast(KafkaEvent event) {
        return (R) event;
    }

    public <R extends KafkaEvent> R cast(KafkaEvent event, Class<R> classType) throws ClassCastException{
        if (!classType.isInstance(event)) {
            throw new ClassCastException("Cannot cast " + event.getClass() + " to " + classType);
        }

        return classType.cast(event);
    }

}
