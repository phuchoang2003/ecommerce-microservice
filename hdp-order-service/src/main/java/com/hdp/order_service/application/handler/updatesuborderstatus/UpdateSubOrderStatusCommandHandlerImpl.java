package com.hdp.order_service.application.handler.updatesuborderstatus;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusCommand;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusCommandHandler;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusResult;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateSubOrderStatusCommandHandlerImpl implements UpdateSubOrderStatusCommandHandler {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional
    public UpdateSubOrderStatusResult handle(UpdateSubOrderStatusCommand command) {
        SubOrder entity = subOrderPersistence.getById(command.id());
        if (entity == null) {
            throw new NotFoundException("SubOrder", command.id());
        }

        entity.updateStatus(command.status());
        SubOrder saved = subOrderPersistence.save(entity);

        log.info("SubOrder status updated: subOrderId={}, status={}", command.id(), command.status());
        return toResult(saved);
    }

    private UpdateSubOrderStatusResult toResult(SubOrder entity) {
        return new UpdateSubOrderStatusResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}
