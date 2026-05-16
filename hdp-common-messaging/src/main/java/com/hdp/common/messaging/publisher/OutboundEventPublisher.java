package com.hdp.common.messaging.publisher;

public interface OutboundEventPublisher {
    void send(Object event, String topic, String key);
}