package com.hdp.common.persistence.event;

import com.hdp.core.event.DomainEventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class DomainEventPublisherAutoConfiguration {

    @Bean
    public DomainEventPublisher domainEventPublisher(ApplicationEventPublisher publisher) {
        return new SpringDomainEventPublisher(publisher);
    }
}