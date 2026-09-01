package com.hdp.order_service.application.handler.getsuborderitems;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsQuery;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsQueryHandler;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsResult;
import com.hdp.order_service.application.port.in.getsuborderitems.SubOrderItemView;
import com.hdp.order_service.application.port.out.OrderItemPersistencePort;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSubOrderItemsQueryHandlerImpl implements GetSubOrderItemsQueryHandler {

    private final SubOrderPersistencePort subOrderPersistence;
    private final OrderItemPersistencePort orderItemPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetSubOrderItemsResult handle(GetSubOrderItemsQuery query) {
        subOrderPersistence.findByIdAndNotDeleted(query.subOrderId())
            .orElseThrow(() -> new NotFoundException("SubOrder", query.subOrderId()));
        List<OrderItem> items = orderItemPersistence.findBySubOrderId(query.subOrderId());
        return new GetSubOrderItemsResult(items.stream().map(e -> new SubOrderItemView(
            e.getId(), null, null,
            e.getSellerId(), e.getProductId(), e.getVariantId(),
            e.getProductName(), e.getVariantName(),
            e.getPrice(), e.getQuantity(), e.getSubtotal()
        )).toList());
    }
}
