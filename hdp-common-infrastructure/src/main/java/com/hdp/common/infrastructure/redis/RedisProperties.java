package com.hdp.common.infrastructure.redis;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        String host,
        int port,
        String password,
        int database,
        int commandTimeoutSeconds,
        LettucePoolProperties lettuce
) {
    public RedisProperties {
        if (host == null) host = "localhost";
        if (port == 0) port = 6379;
        if (password == null) password = "";
        if (commandTimeoutSeconds == 0) commandTimeoutSeconds = 2;
    }
}

