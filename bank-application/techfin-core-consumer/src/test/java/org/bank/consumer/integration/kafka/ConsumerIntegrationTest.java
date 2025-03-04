package org.bank.consumer.integration.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.bank.core.kafka.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@SpringJUnitConfig
@ActiveProfiles("test")
public class ConsumerIntegrationTest extends EmbeddedKafkaTest {

    @Autowired
    protected KafkaTemplate<String, KafkaEvent> kafkaTemplate = kafkaTemplate();

    public KafkaTemplate<String, KafkaEvent> kafkaTemplate() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producerProps.put("key.serializer", StringSerializer.class);
        producerProps.put("value.serializer", KafkaEventSerializer.class);

        ProducerFactory<String, KafkaEvent> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        return new KafkaTemplate<>(producerFactory);
    }
}
