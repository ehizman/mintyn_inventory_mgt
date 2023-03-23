package com.ehizman.inventorymgt.unit_tests;

import com.ehizman.inventorymgt.kafka.KafkaConsumerConfig;
import com.jayway.jsonpath.internal.filter.ValueNodes;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
@EmbeddedKafka(partitions = 1, controlledShutdown = true, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class KafkaConsumerConfigTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private Environment env;

    private final EmbeddedKafkaBroker embeddedKafka = new EmbeddedKafkaBroker(1, true, "test-topic");

    @Test
    public void testUserConsumerFactory() {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(env, kafkaTemplate);
        ConsumerFactory<String, String> consumerFactory = kafkaConsumerConfig.userConsumerFactory();
        assertNotNull(consumerFactory);
    }

    @Test
    @Transactional
    public void testOrderKafkaListenerContainerFactory() throws Exception {
        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(env, kafkaTemplate);
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaConsumerConfig.orderKafkaListenerContainerFactory();
        assertNotNull(factory);

        // send a message to the test topic
        String testTopic = "process";
        String testMessage = "\"message\":\"test-message\"";
        ProducerRecord<String, String> record = new ProducerRecord<>(testTopic, testMessage);
        kafkaTemplate.send(record).get();

        // create a consumer and subscribe to the test topic
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("spring.kafka.consumer.group-id"));
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProps,  new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(Collections.singletonList(testTopic));

        // wait for the consumer to receive the message
        ConsumerRecords<String, String> consumerRecords = null;
        int attempts = 0;
        while (attempts < 5) {
            consumerRecords = consumer.poll(Duration.ofSeconds(1));
            if (!consumerRecords.isEmpty()) {
                break;
            }
            attempts++;
        }

        // assert that the message was received
        assertFalse(consumerRecords.isEmpty());
        Iterator<ConsumerRecord<String, String>> recordIterator = consumerRecords.iterator();
        ConsumerRecord<String, String> receivedRecord = recordIterator.next();
        assertEquals(testMessage, receivedRecord.value());
    }
}
