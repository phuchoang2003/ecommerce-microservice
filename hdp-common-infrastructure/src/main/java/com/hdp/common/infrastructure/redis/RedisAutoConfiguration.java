package com.hdp.common.infrastructure.redis;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
// TODO: Try redis sentinel and cluster
public class RedisAutoConfiguration {
    private final RedisProperties properties;

    private static final int MAX_TOTAL_CONNECTIONS = 20;

    public RedisAutoConfiguration(RedisProperties properties) {
        this.properties = properties;
    }



//    SENTINEL CONFIG
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("sentinel1.example.com", 26379)
//                .sentinel("sentinel2.example.com", 26379)
//                .sentinel("sentinel3.example.com", 26379);
//
//        sentinelConfig.setPassword(RedisPassword.of("yourpassword"));
//
//        return new LettuceConnectionFactory(sentinelConfig);
//    }


//    CLUSTER CONFIG
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(
//                List.of(
//                        "redis1.example.com:6379",
//                        "redis2.example.com:6379",
//                        "redis3.example.com:6379"
//                )
//        );
//        clusterConfig.setMaxRedirects(3);
//        clusterConfig.setPassword(RedisPassword.of("yourpassword"));
//
//        return new LettuceConnectionFactory(clusterConfig);
//    }



//    STANDALONE CONFIG
    @Bean
    public LettuceConnectionFactory  redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(properties.host());
        config.setPort(properties.port());
        config.setPassword(RedisPassword.of(properties.password()));
        config.setDatabase(properties.database());


        // Connection pool configuration
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        poolConfig.setMaxIdle(properties.lettuce().pool().maxIdle());
        poolConfig.setMinIdle(properties.lettuce().pool().minIdle());
        poolConfig.setMaxWait(Duration.ofSeconds(properties.lettuce().pool().maxWait()));
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);

        // Lettuce client configuration
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .commandTimeout(Duration.ofSeconds(properties.commandTimeoutSeconds()))
                .shutdownTimeout(Duration.ofMillis(properties.lettuce().shutdownTimeoutMs()))
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());


        // Value serializer (JSON)
        GenericJacksonJsonRedisSerializer jsonSerializer =
                GenericJacksonJsonRedisSerializer.builder().build();

        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

}
