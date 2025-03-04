package org.bank.consumer.integration.kafka;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaZKBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@EmbeddedKafka(
        topics = { "user.account.created",
                "family.invitation", "family.cash.conversion", "family.payment.request", "family.payment.approval",
                "pay.cachable.charged", "pay.product.purchased" },
        partitions = 2,
        brokerProperties = {
            "listeners=PLAINTEXT://localhost:9092"
        },
        ports = {9092})
@EnableKafka
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:/application-kafka-consumer.properties")
@EnableConfigurationProperties(KafkaProperties.class)
public abstract class EmbeddedKafkaTest {

        @Autowired
        protected EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaZKBroker(1);

        protected final KafkaTopicMonitor kafkaTopicMonitor = new KafkaTopicMonitor();
}
