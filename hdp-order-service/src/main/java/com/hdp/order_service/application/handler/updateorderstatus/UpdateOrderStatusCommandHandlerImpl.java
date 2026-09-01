package com.hdp.order_service.application.handler.updateorderstatus;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusCommand;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusCommandHandler;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusResult;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.application.port.out.OrderStatusHistoryPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateOrderStatusCommandHandlerImpl implements UpdateOrderStatusCommandHandler {

    private final OrderPersistencePort orderPersistence;
    private final OrderStatusHistoryPersistencePort historyPersistence;

    @Override
    @Transactional
    public UpdateOrderStatusResult handle(UpdateOrderStatusCommand command) {
        Order order = orderPersistence.getById(command.id());
        if (order == null) {
            throw new NotFoundException("Order", command.id());
        }

        order.updateStatus(command.status(), command.changedBy(), command.reason());
        Order saved = orderPersistence.save(order);

        OrderStatusHistory latestHistory = saved.getStatusHistories().isEmpty()
            ? null : saved.getStatusHistories().getLast();
        if (latestHistory != null) {
            historyPersistence.save(latestHistory);
        }

        log.info("Order status updated: orderId={}, newStatus={}", command.id(), command.status());
        return toResult(saved);
    }

    private UpdateOrderStatusResult toResult(Order entity) {
        return new UpdateOrderStatusResult(
            entity.getId().value(), entity.getOrderNumber().value(), entity.getBuyerId(), entity.getShippingAddressId(),
            entity.getPaymentMethod(), entity.getStatus(),
            entity.getSubtotal(), entity.getShippingFee(), entity.getDiscount(), entity.getTax(),
            entity.getTotalAmount(), entity.getPaymentIntentId(),
            entity.getExpiresAt(), entity.getPaidAt(), entity.getCancelledAt(), entity.getCancellationReason(),
            entity.getCreatedAt(), entity.getUpdatedAt(),
            entity.getSubOrders().stream().map(this::toSubOrder).toList(),
            entity.getItems().stream().map(this::toItem).toList(),
            entity.getAppliedCoupons().stream().map(this::toCoupon).toList()
        );
    }

    private UpdateOrderStatusResult.SubOrderResult toSubOrder(SubOrder entity) {
        return new UpdateOrderStatusResult.SubOrderResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private UpdateOrderStatusResult.OrderItemResult toItem(OrderItem entity) {
        return new UpdateOrderStatusResult.OrderItemResult(
            entity.getId(), null, null,
            entity.getSellerId(), entity.getProductId(), entity.getVariantId(),
            entity.getProductName(), entity.getVariantName(),
            entity.getPrice(), entity.getQuantity(), entity.getSubtotal()
        );
    }

    private UpdateOrderStatusResult.AppliedCouponResult toCoupon(AppliedCoupon entity) {
        return new UpdateOrderStatusResult.AppliedCouponResult(
            entity.getId(), null,
            entity.getCode(), entity.getCouponType(), entity.getDiscountValue()
        );
    }
}
