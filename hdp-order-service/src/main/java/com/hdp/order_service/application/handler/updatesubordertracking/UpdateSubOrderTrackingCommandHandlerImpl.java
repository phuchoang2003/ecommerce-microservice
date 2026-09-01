package com.hdp.order_service.application.handler.updatesubordertracking;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingCommand;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingCommandHandler;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingResult;
import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateSubOrderTrackingCommandHandlerImpl implements UpdateSubOrderTrackingCommandHandler {

    private final SubOrderPersistencePort subOrderPersistence;

    @Override
    @Transactional
    public UpdateSubOrderTrackingResult handle(UpdateSubOrderTrackingCommand command) {
        SubOrder entity = subOrderPersistence.getById(command.id());
        if (entity == null) {
            throw new NotFoundException("SubOrder", command.id());
        }

        entity.updateTracking(command.trackingNumber(), command.carrier(), command.estimatedDelivery());
        if (command.note() != null) {
            entity.updateNote(command.note());
        }

        SubOrder saved = subOrderPersistence.save(entity);
        log.info("SubOrder tracking updated: subOrderId={}", command.id());
        return toResult(saved);
    }

    private UpdateSubOrderTrackingResult toResult(SubOrder entity) {
        return new UpdateSubOrderTrackingResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}
