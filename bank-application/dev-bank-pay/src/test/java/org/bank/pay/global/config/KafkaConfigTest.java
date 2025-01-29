package org.bank.pay.global.config;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bank.core.kafka.KafkaEvent;
import org.bank.pay.core.domain.familly.MemberClaims;
import org.bank.pay.core.event.family.kafka.InviteEvent;
import org.bank.pay.global.kafka.KafkaEventSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KafkaConfigTest.TestConfig.class, KafkaConfigTest.KafkaConfig.class})
class KafkaConfigTest {

    @Configuration
    @ComponentScan(basePackages = {
            "org.bank.pay.core",
            "org.bank.pay.global",
            "org.bank.store.mysql.core.pay"
    })
    static class TestConfig { }

    @Configuration
    @PropertySource("classpath:application-kafka-producer.properties")
    @EnableConfigurationProperties(KafkaProperties.class)
    static class KafkaConfig { }


    @Autowired
    private KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    @Autowired
    private KafkaProperties kafkaProperties;

    private final MemberClaims follower = new MemberClaims("fixture-id", "fixture", "fixture@email.com");

    @Test
    @DisplayName("카프카 프로듀서 설정을 등록한다")
    void 카프카_프로듀서_설정을_등록한다() {

        KafkaProperties.Producer producerProperties = kafkaProperties.getProducer();

        Assertions.assertAll(
                () -> assertThat(producerProperties.getBootstrapServers().get(0)).isEqualTo("localhost:9092"),
                () -> assertThat(producerProperties.getKeySerializer()).isEqualTo(StringSerializer.class),
                () -> assertThat(producerProperties.getValueSerializer()).isEqualTo(KafkaEventSerializer.class),
                () -> assertThat(producerProperties.getAcks()).isEqualTo("1")
        );

    }

    @Test
    @DisplayName("event를 카프카 토픽으로 전송한다.")
    void event를_카프카_토픽으로_전송한다() throws ExecutionException, InterruptedException {
        SendResult<String, KafkaEvent> result = kafkaTemplate.send("family.invitation", new InviteEvent(UUID.randomUUID(), follower)).get();
        RecordMetadata record = result.getRecordMetadata();
        assertThat(record.topic()).isEqualTo("family.invitation");
    }

}