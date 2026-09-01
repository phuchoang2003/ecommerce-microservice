package com.hdp.order_service.application.handler.getsuborder;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderQuery;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderQueryHandler;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderResult;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetSubOrderQueryHandlerImpl implements GetSubOrderQueryHandler {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetSubOrderResult handle(GetSubOrderQuery query) {
        SubOrder entity = subOrderPersistence.findByIdAndNotDeleted(query.id())
            .orElseThrow(() -> new NotFoundException("SubOrder", query.id()));
        return toResult(entity);
    }

    private GetSubOrderResult toResult(SubOrder entity) {
        return new GetSubOrderResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}
