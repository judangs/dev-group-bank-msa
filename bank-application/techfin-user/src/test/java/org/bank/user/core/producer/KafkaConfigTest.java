package org.bank.user.core.producer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KafkaConfigTest.IntegrationTest.class)
public class KafkaConfigTest {

    @Configuration
    @Import(KafkaConfig.class)
    @PropertySource("classpath:application-kafka-producer.properties")
    @EnableConfigurationProperties(KafkaProperties.class)
    static class IntegrationTest { }

    @Autowired
    private KafkaProperties kafkaProperties;

    @Test
    void 프로퍼티_속성을_조회합니다() {
        Assertions.assertThat(kafkaProperties.getProducer().getAcks()).isNotNull();
    }
}
