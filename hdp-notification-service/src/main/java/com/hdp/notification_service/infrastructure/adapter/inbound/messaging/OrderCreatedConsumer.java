package com.hdp.notification_service.infrastructure.adapter.inbound.messaging;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {
    @KafkaListener(topics = "my_topic", groupId = "group_id")
    public void consume(String message, Acknowledgment acknowledgment) {
        System.out.println("Message received: " + message);
//        throw new RuntimeException("Not implemented");
    }
}
