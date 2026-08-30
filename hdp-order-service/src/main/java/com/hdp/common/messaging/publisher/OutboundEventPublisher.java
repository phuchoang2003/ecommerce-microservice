package com.hdp.common.messaging.publisher;

import java.util.concurrent.TimeUnit;

public interface OutboundEventPublisher {
    void send(Object event, String topic, String key);
    void sendAckWait(Object event, String topic, String key, int timeout, TimeUnit timeUnit) throws Exception;
}