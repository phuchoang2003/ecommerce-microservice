package com.hdp.order_service.application.handler.createorder;

import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommandHandler;
import com.hdp.order_service.application.port.in.createorder.CreateOrderResult;
import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.event.OrderCreatedDomainEvent;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import com.hdp.order_service.domain.model.SubOrder;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandlerImpl implements CreateOrderCommandHandler {

    private final OrderPersistencePort orderPersistence;
    private final DomainEventPublisher eventPublisher;
    private final ProductionSnapshotPersistencePort productionSnapshotPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResult handle(CreateOrderCommand command) {
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

        Map<UUID, List<CreateOrderCommand.CreateOrderItemCommand>> itemsBySeller = command.items().stream()
            .collect(Collectors.groupingBy(CreateOrderCommand.CreateOrderItemCommand::sellerId));

        for (Map.Entry<UUID, List<CreateOrderCommand.CreateOrderItemCommand>> entry : itemsBySeller.entrySet()) {
            UUID sellerId = entry.getKey();
            List<CreateOrderCommand.CreateOrderItemCommand> sellerItems = entry.getValue();

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

    private Map<UUID, ProductSnapshot> validateProducts(List<CreateOrderCommand.CreateOrderItemCommand> items) {
        List<UUID> productIds = items.stream()
            .map(CreateOrderCommand.CreateOrderItemCommand::productId)
            .distinct()
            .toList();

        Map<UUID, ProductSnapshot> snapshotMap = productionSnapshotPersistence.findByProductIdIn(productIds);

        for (CreateOrderCommand.CreateOrderItemCommand item : items) {
            if (!snapshotMap.containsKey(item.productId())) {
                throw new NotFoundException("Product", item.productId());
            }
        }
        log.info("Validated {} products for order creation", productIds.size());
        return snapshotMap;
    }

    private CreateOrderResult toResult(Order entity) {
        return new CreateOrderResult(
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

    private CreateOrderResult.SubOrderResult toSubOrderResult(SubOrder entity) {
        return new CreateOrderResult.SubOrderResult(
            entity.getId(), entity.getOrderId(),
            entity.getSellerId(), entity.getSellerName(),
            entity.getStatus(), entity.getTrackingNumber(), entity.getCarrier(),
            entity.getEstimatedDelivery(), entity.getNote(), entity.getProcessedAt(), entity.getShippedAt(),
            entity.getItems() != null ? entity.getItems().size() : 0,
            entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }

    private CreateOrderResult.OrderItemResult toOrderItemResult(OrderItem entity) {
        return new CreateOrderResult.OrderItemResult(
            entity.getId(), null, null,
            entity.getSellerId(), entity.getProductId(), entity.getVariantId(),
            entity.getProductName(), entity.getVariantName(),
            entity.getPrice(), entity.getQuantity(), entity.getSubtotal()
        );
    }

    private CreateOrderResult.AppliedCouponResult toAppliedCouponResult(AppliedCoupon entity) {
        return new CreateOrderResult.AppliedCouponResult(
            entity.getId(), null,
            entity.getCode(), entity.getCouponType(), entity.getDiscountValue()
        );
    }
}
