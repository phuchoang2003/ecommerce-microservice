package com.hdp.order_service.application.handler.listsubordersbyorder;

import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderQuery;
import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderQueryHandler;
import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderResult;
import com.hdp.order_service.application.port.in.listsubordersbyorder.SubOrderSummary;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListSubOrdersByOrderQueryHandlerImpl implements ListSubOrdersByOrderQueryHandler {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional(readOnly = true)
    public ListSubOrdersByOrderResult handle(ListSubOrdersByOrderQuery query) {
        List<SubOrder> results = subOrderPersistence.findByOrderId(query.orderId());
        return new ListSubOrdersByOrderResult(results.stream().map(e -> new SubOrderSummary(
            e.getId(), e.getOrderId(),
            e.getSellerId(), e.getSellerName(), e.getStatus(), e.getCreatedAt()
        )).toList());
    }
}
