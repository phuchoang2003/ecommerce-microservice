package com.hdp.order_service.infrastructure.adapter.outbound.messaging;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final OutboundEventPublisher publisher;

    public <T> void send(T event, String topic, String key) {
        publisher.send(event, topic, key);
    }
}
