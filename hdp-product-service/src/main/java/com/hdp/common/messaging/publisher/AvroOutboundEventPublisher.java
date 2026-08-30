package com.hdp.common.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AvroOutboundEventPublisher implements OutboundEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AvroOutboundEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(Object event, String topic, String key) {
            kafkaTemplate.send(topic, key, event);
        log.info("Avro message sent to topic {}: eventType={} key={}", topic, event.getClass().getSimpleName(), key);
    }


    @Override
    public void sendAckWait(Object event, String topic, String key, int timeout, TimeUnit timeUnit) throws Exception {
        kafkaTemplate.send(topic, key, event)
                .get(timeout, timeUnit); // Wait for broker to send ACK, timeout after n seconds => wait for message to be received
        log.info("Avro message sent to topic {}: eventType={} key={}", topic, event.getClass().getSimpleName(), key);
    }
}
