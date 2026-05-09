package com.hdp.order_service.application.usecase;

import com.hdp.order_service.application.port.in.ListOrdersUsecase;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListOrdersUsecaseImpl implements ListOrdersUsecase {

    private final OrderPersistencePort orderPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        var results = orderPersistence.findAll(command.buyerId(), command.status(), command.page(), command.size());
        return new Result(results.stream().map(this::toSummary).toList());
    }

    private OrderSummary toSummary(Order e) {
        return new OrderSummary(
            e.getId(), e.getOrderNumber(), e.getBuyerId(),
            e.getPaymentMethod(), e.getStatus(), e.getTotalAmount(), e.getCreatedAt()
        );
    }
}