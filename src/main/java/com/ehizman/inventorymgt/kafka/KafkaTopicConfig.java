package com.ehizman.inventorymgt.kafka;

import jakarta.validation.constraints.Null;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Objects;

@Configuration
public class KafkaTopicConfig {
    private final Environment env;

    public KafkaTopicConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public NewTopic processedOrderTopic() {
        try{
            int processTopicPartitions = Integer.parseInt(Objects.requireNonNull(env.getProperty("process.partitions")));
            return TopicBuilder.name("process").partitions(processTopicPartitions).build();
        } catch (NullPointerException nullPointerException){
            throw new KafkaException("partition and replica values not set");
        }
    }

    @Bean
    public NewTopic deadOrderTopic() {
        try{
            return TopicBuilder.name("dead-order").partitions(1)
                    .config(TopicConfig.RETENTION_MS_CONFIG, Objects.requireNonNull(env.getProperty("dead.order.retention")))
                    .build();
        } catch (NullPointerException nullPointerException){
            throw new KafkaException("partition and replica values not set");
        }
    }
}
