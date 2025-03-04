package org.bank.consumer.global.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

@RequiredArgsConstructor
public class KafkaDynamicPropertyInjector {

    private final KafkaProperties kafkaProperties;
    private final KafkaDynamicProperties kafkaDynamicProperties;
    @Getter
    private final Map<String, Object> consumerProps;

    public void inject(EventType type) {
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumer().getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaDynamicProperties.group(type));
    }

    public String[] topic(EventType type) {
        return kafkaDynamicProperties.topic(type);
    }

    public void inject(ContainerProperties containerProperties) {
        containerProperties.setAckMode(kafkaProperties.getListener().getAckMode());
        containerProperties.setPollTimeout(kafkaProperties.getListener().getPollTimeout().toMillis());
    }

    public void inject(ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> concurrentKafkaListenerContainerFactory, ConsumerFactory<String, KafkaEvent> consumerFactory) {
        concurrentKafkaListenerContainerFactory.setConcurrency(2);
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        inject(concurrentKafkaListenerContainerFactory.getContainerProperties());
    }
}
