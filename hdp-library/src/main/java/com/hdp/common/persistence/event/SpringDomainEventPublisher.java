package com.hdp.common.persistence.event;


import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.event.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;

public class SpringDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher publisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }
}