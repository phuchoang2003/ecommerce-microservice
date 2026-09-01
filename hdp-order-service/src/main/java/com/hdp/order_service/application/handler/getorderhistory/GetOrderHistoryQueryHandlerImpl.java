package com.hdp.order_service.application.handler.getorderhistory;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryQuery;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryQueryHandler;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryResult;
import com.hdp.order_service.application.port.in.getorderhistory.OrderStatusHistoryView;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.application.port.out.OrderStatusHistoryPersistencePort;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetOrderHistoryQueryHandlerImpl implements GetOrderHistoryQueryHandler {

    private final OrderPersistencePort orderPersistence;
    private final OrderStatusHistoryPersistencePort historyPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetOrderHistoryResult handle(GetOrderHistoryQuery query) {
        orderPersistence.findByIdAndNotDeleted(query.orderId())
            .orElseThrow(() -> new NotFoundException("Order", query.orderId()));
        List<OrderStatusHistory> histories = historyPersistence.findByOrderIdOrderByCreatedAtDesc(query.orderId());
        return new GetOrderHistoryResult(histories.stream().map(h -> new OrderStatusHistoryView(
            h.getId(), null,
            h.getPreviousStatus(), h.getNewStatus(),
            h.getChangedBy(), h.getReason(), h.getCreatedAt()
        )).toList());
    }
}
