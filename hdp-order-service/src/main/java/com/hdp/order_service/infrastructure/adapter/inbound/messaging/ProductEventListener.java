package com.hdp.order_service.infrastructure.adapter.inbound.messaging;

import com.hdp.common.messaging.dispatcher.AvroEventDispatcher;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.internals.Acknowledgements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class ProductEventListener {

    private static final Logger log = LoggerFactory.getLogger(ProductEventListener.class);

    private final AvroEventDispatcher dispatcher;

    public ProductEventListener(AvroEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @KafkaListener(topics = "product.events", groupId = "order-service")
    public void consume(SpecificRecord event, Acknowledgment acknowledgment) {
        log.info("Received event: class={}, schema={}", event.getClass().getName(), event.getSchema().getName());
        try {
            dispatcher.dispatch(event);
            acknowledgment.acknowledge();
            log.info("Event dispatched successfully: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Failed to dispatch event: {}", e.getMessage(), e);
        }
    }
}
