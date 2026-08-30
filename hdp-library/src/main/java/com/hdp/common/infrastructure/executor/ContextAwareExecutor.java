package com.hdp.common.infrastructure.executor;

import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class ContextAwareExecutor implements Executor, AutoCloseable {

    private final ExecutorService delegate;
    private final TaskDecorator decorator;

    public ContextAwareExecutor(ExecutorService delegate, TaskDecorator decorator) {
        this.delegate = delegate;
        this.decorator = decorator;
    }

    @Override
    public void execute(Runnable command) {
        delegate.execute(decorator.decorate(command));
    }

    @Override
    public void close() {
        delegate.close();
    }
}