package com.hdp.order_service.application.handler.getorderitems;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsQuery;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsQueryHandler;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsResult;
import com.hdp.order_service.application.port.in.getorderitems.OrderItemView;
import com.hdp.order_service.application.port.out.OrderItemPersistencePort;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrderItemsQueryHandlerImpl implements GetOrderItemsQueryHandler {

    private final OrderPersistencePort orderPersistence;
    private final OrderItemPersistencePort orderItemPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetOrderItemsResult handle(GetOrderItemsQuery query) {
        orderPersistence.findByIdAndNotDeleted(query.orderId())
            .orElseThrow(() -> new NotFoundException("Order", query.orderId()));
        List<OrderItem> items = orderItemPersistence.findByOrderId(query.orderId());
        return new GetOrderItemsResult(items.stream().map(e -> new OrderItemView(
            e.getId(), null, null,
            e.getSellerId(), e.getProductId(), e.getVariantId(),
            e.getProductName(), e.getVariantName(),
            e.getPrice(), e.getQuantity(), e.getSubtotal()
        )).toList());
    }
}
