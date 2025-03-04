package org.bank.consumer.global.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EventDeserializerContext {

    public static final Set<EventObjectMapper> mappers = ConcurrentHashMap.newKeySet();

    public static ObjectMapper event(EventType type) {
        return mappers.stream().filter(mapper -> mapper.getEventType().equals(type))
                .findFirst().map(EventObjectMapper::getObjectMapper)
                .orElseThrow(IllegalAccessError::new);
    }

    public static ObjectMapper delegation(String topic) {
        return mappers.stream().filter(mapper -> mapper.getTopics().contains(topic))
                .map(EventObjectMapper::getObjectMapper).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static void add(EventType type, String[] topics, ObjectMapper objectMapper) {
        mappers.add(new EventObjectMapper(type, topics, objectMapper));
    }


    @Getter
    public static class EventObjectMapper {
        private final EventType eventType;
        private final List<String> topics;
        private final ObjectMapper objectMapper;

        public EventObjectMapper(EventType eventType, String[] topics, ObjectMapper objectMapper) {
            this.eventType = eventType;
            this.topics = Arrays.asList(topics);
            this.objectMapper = objectMapper;
        }
    }
}
