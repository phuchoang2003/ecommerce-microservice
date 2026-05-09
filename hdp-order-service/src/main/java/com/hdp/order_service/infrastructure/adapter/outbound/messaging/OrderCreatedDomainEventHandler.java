package com.hdp.order_service.infrastructure.adapter.outbound.messaging;


import com.hdp.core.event.DomainEventHandler;
import com.hdp.order_service.domain.event.OrderCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedDomainEventHandler implements DomainEventHandler<OrderCreatedDomainEvent> {

    private final OrderEventPublisher publisher;
    private static final String ORDER_TOPIC = "order-created";

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedDomainEvent event) {
//        publisher.send(integrationEvent, ORDER_TOPIC, event.getOrderId().toString());
        log.info("Published OrderCreatedIntegrationEvent: orderId={}", event.getOrderId());
    }
}
