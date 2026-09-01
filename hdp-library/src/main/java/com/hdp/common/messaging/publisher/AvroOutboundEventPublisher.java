package com.hdp.common.messaging.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class AvroOutboundEventPublisher implements OutboundEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(Object event, String topic, String key) {
        kafkaTemplate.send(topic, key, event);
        log.info("Avro message sent to topic {}: eventType={} key={}", topic, event.getClass().getSimpleName(), key);
    }
}