package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.GetSubOrderItemsUsecase;
import com.hdp.order_service.application.port.out.OrderItemPersistencePort;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSubOrderItemsUsecaseImpl implements GetSubOrderItemsUsecase {

    private final SubOrderPersistencePort subOrderPersistence;
    private final OrderItemPersistencePort orderItemPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        subOrderPersistence.findByIdAndNotDeleted(command.subOrderId())
            .orElseThrow(() -> new NotFoundException("SubOrder", command.subOrderId()));
        List<OrderItem> items = orderItemPersistence.findBySubOrderId(command.subOrderId());
        return new Result(items.stream().map(e -> new OrderItemResult(
            e.getId(), null, null,
            e.getSellerId(), e.getProductId(), e.getVariantId(),
            e.getProductName(), e.getVariantName(),
            e.getPrice(), e.getQuantity(), e.getSubtotal()
        )).toList());
    }
}