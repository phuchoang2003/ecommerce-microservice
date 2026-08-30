package com.hdp.core.event;


public interface DomainEventHandler<T extends DomainEvent>{
    void handle(T event);
}