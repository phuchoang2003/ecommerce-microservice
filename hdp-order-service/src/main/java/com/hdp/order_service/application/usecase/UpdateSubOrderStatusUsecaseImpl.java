package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.UpdateSubOrderStatusUsecase;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateSubOrderStatusUsecaseImpl implements UpdateSubOrderStatusUsecase {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional
    public Result execute(Command command) {
        SubOrder entity = subOrderPersistence.getById(command.id());
        if (entity == null) {
            throw new NotFoundException("SubOrder", command.id());
        }

        entity.updateStatus(command.status());
        SubOrder saved = subOrderPersistence.save(entity);

        log.info("SubOrder status updated: subOrderId={}, status={}", command.id(), command.status());
        return toResult(saved);
    }

    private Result toResult(SubOrder entity) {
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
