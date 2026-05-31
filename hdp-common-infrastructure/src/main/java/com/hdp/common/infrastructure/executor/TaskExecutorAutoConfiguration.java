package com.hdp.common.infrastructure.executor;


import com.hdp.common.infrastructure.constants.TaskExecutorConstants;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@AutoConfiguration
@EnableAsync
@EnableConfigurationProperties(CpuPoolProperties.class)
public class TaskExecutorAutoConfiguration {
    private final String IO_BOUND_THREAD_NAME = "io-vt-";
    private final String CPU_BOUND_THREAD_NAME = "cpu-vt-";
    private final int THREAD_START_INDEX = 0;
    private final CpuPoolProperties cpuPoolProperties;

    public TaskExecutorAutoConfiguration(CpuPoolProperties cpuPoolProperties) {
        this.cpuPoolProperties = cpuPoolProperties;
    }

    @Bean(TaskExecutorConstants.IO_BOUND)
    public Executor taskIOExecutor() {
        return new ContextAwareExecutor(
                Executors.newThreadPerTaskExecutor(
                        Thread.ofVirtual()
                                .name(IO_BOUND_THREAD_NAME, THREAD_START_INDEX)
                                .factory()
                ),
                new RequestContextTaskDecorator()
        );
    }

    @Bean(TaskExecutorConstants.CPU_BOUND)
    public Executor taskCPUExecutor() {
        return new ThreadPoolExecutor(
                cpuPoolProperties.coreSize(),
                cpuPoolProperties.maxSize(),
                cpuPoolProperties.keepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(cpuPoolProperties.queueCapacity()),
                Thread.ofPlatform()
                        .name(CPU_BOUND_THREAD_NAME, THREAD_START_INDEX)
                        .factory()
        );
    }
}