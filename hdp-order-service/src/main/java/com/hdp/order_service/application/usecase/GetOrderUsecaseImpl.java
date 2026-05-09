package com.hdp.order_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.GetOrderUsecase;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.domain.model.SubOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GetOrderUsecaseImpl implements GetOrderUsecase {

    private final OrderPersistencePort orderPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        Order order = orderPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Order", command.id()));
        return toResult(order);
    }

    private Result toResult(Order entity) {
        return new Result(
            entity.getId(), entity.getOrderNumber(), entity.getBuyerId(), entity.getShippingAddressId(),
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

    private SubOrderResult toSubOrder(SubOrder entity) {
        return new SubOrderResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private OrderItemResult toItem(OrderItem entity) {
        return new OrderItemResult(
            entity.getId(), null, null,
            entity.getSellerId(), entity.getProductId(), entity.getVariantId(),
            entity.getProductName(), entity.getVariantName(),
            entity.getPrice(), entity.getQuantity(), entity.getSubtotal()
        );
    }

    private AppliedCouponResult toCoupon(AppliedCoupon entity) {
        return new AppliedCouponResult(
            entity.getId(), null,
            entity.getCode(), entity.getCouponType(), entity.getDiscountValue()
        );
    }
}