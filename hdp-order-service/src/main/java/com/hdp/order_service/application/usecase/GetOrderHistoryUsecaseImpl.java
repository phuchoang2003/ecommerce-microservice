package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.GetOrderHistoryUsecase;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.application.port.out.OrderStatusHistoryPersistencePort;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrderHistoryUsecaseImpl implements GetOrderHistoryUsecase {

    private final OrderPersistencePort orderPersistence;
    private final OrderStatusHistoryPersistencePort historyPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        orderPersistence.findByIdAndNotDeleted(command.orderId())
            .orElseThrow(() -> new NotFoundException("Order", command.orderId()));
        List<OrderStatusHistory> histories = historyPersistence.findByOrderIdOrderByCreatedAtDesc(command.orderId());
        return new Result(histories.stream().map(h -> new OrderStatusHistoryResult(
            h.getId(), null,
            h.getPreviousStatus(), h.getNewStatus(),
            h.getChangedBy(), h.getReason(), h.getCreatedAt()
        )).toList());
    }
}