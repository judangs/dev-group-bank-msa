package org.bank.consumer.integration.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.OffsetSpec;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class KafkaTopicMonitor {

    private final static Logger logger = LoggerFactory.getLogger(KafkaTopicMonitor.class);
    private final AdminClient adminClient;

    public KafkaTopicMonitor() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        adminClient = AdminClient.create(props);
    }

    public boolean commit(String topic, String groupId) throws ExecutionException, InterruptedException {
        boolean empty = true;

        ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets(groupId);
        KafkaFuture<Map<TopicPartition, OffsetAndMetadata>> mapKafkaFuture
                = result.partitionsToOffsetAndMetadata();

        for (TopicPartition partition : mapKafkaFuture.get().keySet()) {
            if (partition.topic().equals(topic)) {
                long endOffset = offset(adminClient, partition);

                if (0 == endOffset) {
                    empty = false;
                    break;
                }
            }
        }

        return empty;
    }

    public boolean acks(String topic, String groupId) throws ExecutionException, InterruptedException
    {
        boolean empty = true;

        ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets(groupId);
        KafkaFuture<Map<TopicPartition, OffsetAndMetadata>> mapKafkaFuture
                = result.partitionsToOffsetAndMetadata();

        for (TopicPartition partition : mapKafkaFuture.get().keySet()) {
            if (partition.topic().equals(topic)) {
                long offset = mapKafkaFuture.get().get(partition).offset();
                long endOffset = offset(adminClient, partition);

                if (offset != endOffset) {
                    empty = false;
                    break;
                }
            }
        }

        return empty;
    }

    private long offset(AdminClient adminClient, TopicPartition partition) throws ExecutionException, InterruptedException {
        long endOffset = 0;

        HashMap<TopicPartition, OffsetSpec> topicPartitionOffsets = new HashMap<>();
        topicPartitionOffsets.put(partition, OffsetSpec.latest());
        ListOffsetsResult listOffsetsResult = adminClient.listOffsets(topicPartitionOffsets);
        endOffset = listOffsetsResult.partitionResult(partition).get().offset();

        return endOffset;
    }


}
