package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.GetSubOrderUsecase;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetSubOrderUsecaseImpl implements GetSubOrderUsecase {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        SubOrder entity = subOrderPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("SubOrder", command.id()));
        return new Result(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}