package com.hdp.core.event;


public abstract class AbstractDomainEventHandler<T extends DomainEvent>
        implements DomainEventHandler<T> {

    @Override
    public void handle(T event) {
        try {
            beforeHandle(event);
            doHandle(event);
            afterHandle(event);
        } catch (Exception e) {
            onError(event, e);
            throw e;
        }
    }

    protected void beforeHandle(T event) {}

    protected void afterHandle(T event) {}

    protected void onError(T event, Exception e) {}

    protected abstract void doHandle(T event);
}