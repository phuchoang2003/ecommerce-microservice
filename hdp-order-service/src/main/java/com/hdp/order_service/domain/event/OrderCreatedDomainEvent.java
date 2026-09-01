package com.hdp.order_service.domain.event;

import com.hdp.core.event.DomainEvent;
import com.hdp.order_service.domain.valueobject.OrderId;
import com.hdp.order_service.domain.valueobject.OrderNumber;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class OrderCreatedDomainEvent extends DomainEvent {

    private final OrderId orderId;
    private final OrderNumber orderNumber;
    private final UUID buyerId;
    private final UUID shippingAddressId;
    private final String paymentMethod;
    private final BigDecimal totalAmount;
    private final Instant expiresAt;
    private final List<OrderItemInfo> items;

    @Builder
    public OrderCreatedDomainEvent(
            OrderId orderId,
            OrderNumber orderNumber,
            UUID buyerId,
            UUID shippingAddressId,
            String paymentMethod,
            BigDecimal totalAmount,
            Instant expiresAt,
            List<OrderItemInfo> items
    ) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.buyerId = buyerId;
        this.shippingAddressId = shippingAddressId;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.expiresAt = expiresAt;
        this.items = items;
    }

    @Override
    public String getAggregateId() {
        return orderId.toString();
    }

    @Getter
    public static class OrderItemInfo {
        private final UUID sellerId;
        private final UUID productId;
        private final UUID variantId;
        private final String productName;
        private final String variantName;
        private final BigDecimal price;
        private final int quantity;

        @Builder
        public OrderItemInfo(UUID sellerId, UUID productId, UUID variantId,
                             String productName, String variantName,
                             BigDecimal price, int quantity) {
            this.sellerId = sellerId;
            this.productId = productId;
            this.variantId = variantId;
            this.productName = productName;
            this.variantName = variantName;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
