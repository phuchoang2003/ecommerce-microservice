package com.hdp.common.messaging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hdp.messaging")
public record KafkaMessagingProperties(
        String bootstrapServers,
        String schemaRegistryUrl,
        String consumerGroupId,
        String autoOffsetReset,
        Boolean specificAvroReader
) {
    public KafkaMessagingProperties {
        bootstrapServers = (bootstrapServers != null) ? bootstrapServers : "localhost:9092";
        schemaRegistryUrl = (schemaRegistryUrl != null) ? schemaRegistryUrl : "http://localhost:8085";
        consumerGroupId = (consumerGroupId != null) ? consumerGroupId : "default-group";
        autoOffsetReset = (autoOffsetReset != null) ? autoOffsetReset : "earliest";
        specificAvroReader = (specificAvroReader != null) ? specificAvroReader : Boolean.TRUE;
    }
}