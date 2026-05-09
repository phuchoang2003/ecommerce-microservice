package com.hdp.notification_service.infrastructure.config.messaging;


import org.springframework.boot.context.properties.ConfigurationProperties;



@ConfigurationProperties(prefix = "spring.kafka")
public record KafkaProperties(
        String consumerGroupId,
        String bootstrapServers
){}
