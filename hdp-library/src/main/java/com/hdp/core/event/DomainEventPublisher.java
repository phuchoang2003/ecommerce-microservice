package com.hdp.core.event;


public interface DomainEventPublisher {
   void publish(DomainEvent event);
}