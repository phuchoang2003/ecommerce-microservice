package com.hdp.order_service.application.usecase;

import com.hdp.order_service.application.port.in.ListSubOrdersByOrderUsecase;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListSubOrdersByOrderUsecaseImpl implements ListSubOrdersByOrderUsecase {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        List<SubOrder> results = subOrderPersistence.findByOrderId(command.orderId());
        return new Result(results.stream().map(e -> new SubOrderSummary(
            e.getId(), e.getOrderId(),
            e.getSellerId(), e.getSellerName(), e.getStatus(), e.getCreatedAt()
        )).toList());
    }
}