package com.hdp.common.infrastructure.schedule;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(SchedulerProperties.class)
public class SchedulerAutoConfiguration {

    private final SchedulerProperties properties;

    public SchedulerAutoConfiguration(SchedulerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(
                properties.poolSize(),
                Thread.ofVirtual()
                        .name(properties.threadNamePrefix(), 0)
                        .factory()
        );
        return new ConcurrentTaskScheduler(executor);
    }
}