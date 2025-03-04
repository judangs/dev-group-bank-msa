package org.bank.consumer.global.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spring.kafka.domain")
public class KafkaDynamicProperties {

    private List<Event> events;

    @Data
    public static class Event {
        private EventType type;
        private List<String> topics;
        private String group;
    }


    public String[] topic(EventType type) {
        return events.stream()
                .filter(event -> event.getType().equals(type))
                .findFirst()
                .map(Event::getTopics)
                .map(list -> list.toArray(new String[0]))
                .orElse(new String[0]);
    }




    public String group(EventType type) {
        return events.stream().filter(event -> event.getType().equals(type))
                .findFirst()
                .map(Event::getGroup)
                .orElseThrow(IllegalAccessError::new);
    }
}
