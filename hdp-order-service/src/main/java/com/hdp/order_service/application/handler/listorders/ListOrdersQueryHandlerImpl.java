package com.hdp.order_service.application.handler.listorders;

import com.hdp.order_service.application.port.in.listorders.ListOrdersQuery;
import com.hdp.order_service.application.port.in.listorders.ListOrdersQueryHandler;
import com.hdp.order_service.application.port.in.listorders.ListOrdersResult;
import com.hdp.order_service.application.port.in.listorders.OrderSummary;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ListOrdersQueryHandlerImpl implements ListOrdersQueryHandler {

    private final OrderPersistencePort orderPersistence;

    @Override
    @Transactional(readOnly = true)
    public ListOrdersResult handle(ListOrdersQuery query) {
        var results = orderPersistence.findAll(query.buyerId(), query.status(), query.page(), query.size());
        return new ListOrdersResult(results.stream().map(this::toSummary).toList());
    }

    private OrderSummary toSummary(Order e) {
        return new OrderSummary(
            e.getId().value(), e.getOrderNumber().value(), e.getBuyerId(),
            e.getPaymentMethod(), e.getStatus(), e.getTotalAmount(), e.getCreatedAt()
        );
    }
}
