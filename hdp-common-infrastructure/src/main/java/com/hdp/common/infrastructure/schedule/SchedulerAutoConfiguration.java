package com.hdp.common.infrastructure.schedule;

import com.hdp.common.infrastructure.constants.TaskExecutorConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

public class SchedulerAutoConfiguration implements SchedulingConfigurer {
    private final Executor executor;

    public SchedulerAutoConfiguration(@Qualifier(TaskExecutorConstants.SCHEDULER) Executor executor) {
        this.executor = executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar) {
        registrar.setScheduler(this.executor);
    }
}
