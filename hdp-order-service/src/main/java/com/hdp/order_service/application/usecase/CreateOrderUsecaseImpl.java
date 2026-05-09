package com.hdp.order_service.application.usecase;


import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.CreateOrderUsecase;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.event.OrderCreatedDomainEvent;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import com.hdp.order_service.domain.model.SubOrder;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderUsecaseImpl implements CreateOrderUsecase {

    private final OrderPersistencePort orderPersistence;
    private final DomainEventPublisher eventPublisher;
    private final ProductionSnapshotPersistencePort productionSnapshotPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result execute(Command command) {
        command.validate(command).throwIfInvalid();
        Map<UUID, ProductSnapshot> snapshotMap = validateProducts(command.items());

        BigDecimal subtotal = command.items().stream()
            .map(item -> snapshotMap.get(item.productId()).price().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
            .orderNumber(generateOrderNumber())
            .buyerId(command.buyerId())
            .shippingAddressId(command.shippingAddressId())
            .paymentMethod(command.paymentMethod())
            .status(OrderStatus.PENDING)
            .subtotal(subtotal)
            .shippingFee(BigDecimal.ZERO)
            .discount(BigDecimal.ZERO)
            .tax(BigDecimal.ZERO)
            .totalAmount(subtotal)
            .subOrders(new ArrayList<>())
            .items(new ArrayList<>())
            .appliedCoupons(new ArrayList<>())
            .statusHistories(new ArrayList<>())
            .build();

        Map<UUID, List<CreateOrderItemCommand>> itemsBySeller = command.items().stream()
            .collect(Collectors.groupingBy(CreateOrderItemCommand::sellerId));

        for (Map.Entry<UUID, List<CreateOrderItemCommand>> entry : itemsBySeller.entrySet()) {
            UUID sellerId = entry.getKey();
            List<CreateOrderItemCommand> sellerItems = entry.getValue();

            List<OrderItem> orderItems = sellerItems.stream()
                .map(item -> {
                    ProductSnapshot snapshot = snapshotMap.get(item.productId());
                    return OrderItem.create(
                        item.sellerId(),
                        item.productId(),
                        item.variantId(),
                        snapshot.productName(),
                        snapshot.variantName(),
                        snapshot.price(),
                        item.quantity()
                    );
                })
                .toList();

            SubOrder subOrder = SubOrder.builder()
                .sellerId(sellerId)
                .status(SubOrderStatus.PAID)
                .items(new ArrayList<>(orderItems))
                .build();

            orderItems.forEach(order::addItem);
            order.addSubOrder(subOrder);
        }

        if (command.couponCodes() != null && !command.couponCodes().isEmpty()) {
            for (String code : command.couponCodes()) {
                AppliedCoupon coupon = AppliedCoupon.create(code, "PERCENT", BigDecimal.ZERO);
                order.addCoupon(coupon);
            }
        }

        OrderStatusHistory history = OrderStatusHistory.create(null, OrderStatus.PENDING, command.buyerId(), "Order created");
        order.getStatusHistories().add(history);

        Order saved = orderPersistence.save(order);

        publishOrderCreatedEvent(saved);

        log.info("Order created: orderId={}, orderNumber={}", saved.getId(), saved.getOrderNumber());
        return toResult(saved);
    }

    private void publishOrderCreatedEvent(Order order) {
        List<OrderCreatedDomainEvent.OrderItemInfo> items = order.getItems().stream()
            .map(item -> OrderCreatedDomainEvent.OrderItemInfo.builder()
                .sellerId(item.getSellerId())
                .productId(item.getProductId())
                .variantId(item.getVariantId())
                .productName(item.getProductName())
                .variantName(item.getVariantName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build())
            .toList();

        OrderCreatedDomainEvent event = OrderCreatedDomainEvent.builder()
            .orderId(order.getId())
            .orderNumber(order.getOrderNumber())
            .buyerId(order.getBuyerId())
            .shippingAddressId(order.getShippingAddressId())
            .paymentMethod(order.getPaymentMethod().name())
            .totalAmount(order.getTotalAmount())
            .expiresAt(order.getExpiresAt())
            .items(items)
            .build();

        eventPublisher.publish(event);
        log.info("Published OrderCreatedDomainEvent: orderId={}", order.getId());
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis() + "-"
            + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Map<UUID, ProductSnapshot> validateProducts(List<CreateOrderItemCommand> items) {
        List<UUID> productIds = items.stream()
            .map(CreateOrderItemCommand::productId)
            .distinct()
            .toList();

        Map<UUID, ProductSnapshot> snapshotMap = productionSnapshotPersistence.findByProductIdIn(productIds);

        for (CreateOrderItemCommand item : items) {
            if (!snapshotMap.containsKey(item.productId())) {
                throw new NotFoundException("Product", item.productId());
            }
        }
        log.info("Validated {} products for order creation", productIds.size());
        return snapshotMap;
    }

    private Result toResult(Order entity) {
        return new Result(
            entity.getId(), entity.getOrderNumber(), entity.getBuyerId(), entity.getShippingAddressId(),
            entity.getPaymentMethod(), entity.getStatus(),
            entity.getSubtotal(), entity.getShippingFee(), entity.getDiscount(), entity.getTax(),
            entity.getTotalAmount(), entity.getPaymentIntentId(),
            entity.getExpiresAt(), entity.getPaidAt(), entity.getCancelledAt(), entity.getCancellationReason(),
            entity.getCreatedAt(), entity.getUpdatedAt(),
            entity.getSubOrders().stream().map(this::toSubOrderResult).toList(),
            entity.getItems().stream().map(this::toOrderItemResult).toList(),
            entity.getAppliedCoupons().stream().map(this::toAppliedCouponResult).toList()
        );
    }

    private SubOrderResult toSubOrderResult(SubOrder entity) {
        return new SubOrderResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private OrderItemResult toOrderItemResult(OrderItem entity) {
        return new OrderItemResult(
            entity.getId(), null, null,
            entity.getSellerId(), entity.getProductId(), entity.getVariantId(),
            entity.getProductName(), entity.getVariantName(),
            entity.getPrice(), entity.getQuantity(), entity.getSubtotal()
        );
    }

    private AppliedCouponResult toAppliedCouponResult(AppliedCoupon entity) {
        return new AppliedCouponResult(
            entity.getId(), null,
            entity.getCode(), entity.getCouponType(), entity.getDiscountValue()
        );
    }
}
