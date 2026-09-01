package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.order_service.application.port.out.OrderPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import com.hdp.order_service.domain.model.SubOrder;
import com.hdp.order_service.domain.valueobject.OrderStatus;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.AppliedCouponJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderItemJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderStatusHistoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.SubOrderJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.OrderRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderPersistencePort {

    private final OrderRepositoryJpa orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order save(Order order) {
        OrderJpa jpa = buildOrderJpa(order);
        jpa = orderRepository.save(jpa);
        return orderMapper.toDomain(jpa);
    }

    private OrderJpa buildOrderJpa(Order order) {
        OrderJpa jpa = OrderJpa.builder()
                .orderNumber(order.getOrderNumber().value())
                .buyerId(order.getBuyerId())
                .shippingAddressId(order.getShippingAddressId())
                .paymentMethod(order.getPaymentMethod())
                .status(order.getStatus())
                .subtotal(order.getSubtotal())
                .shippingFee(order.getShippingFee())
                .discount(order.getDiscount())
                .tax(order.getTax())
                .totalAmount(order.getTotalAmount())
                .paymentIntentId(order.getPaymentIntentId())
                .expiresAt(order.getExpiresAt())
                .paidAt(order.getPaidAt())
                .cancelledAt(order.getCancelledAt())
                .cancellationReason(order.getCancellationReason())
                .subOrders(new ArrayList<>())
                .orderItems(new ArrayList<>())
                .appliedCoupons(new ArrayList<>())
                .statusHistories(new ArrayList<>())
                .build();

        if (order.getId() != null) {
            jpa.setId(order.getId().value());
        }

        for (SubOrder subOrder : order.getSubOrders()) {
            SubOrderJpa subOrderJpa = buildSubOrderJpa(subOrder, jpa);
            jpa.getSubOrders().add(subOrderJpa);

            for (OrderItem item : subOrder.getItems()) {
                OrderItemJpa itemJpa = orderMapper.toOrderItemJpa(item);
                itemJpa.setOrder(jpa);
                itemJpa.setSubOrder(subOrderJpa);
                jpa.getOrderItems().add(itemJpa);
            }
        }

        for (OrderItem item : order.getItems()) {
            if (item.getSubtotal() == null) {
                OrderItemJpa itemJpa = orderMapper.toOrderItemJpa(item);
                itemJpa.setOrder(jpa);
                jpa.getOrderItems().add(itemJpa);
            }
        }

        for (AppliedCoupon coupon : order.getAppliedCoupons()) {
            AppliedCouponJpa couponJpa = orderMapper.toAppliedCouponJpa(coupon);
            couponJpa.setOrder(jpa);
            jpa.getAppliedCoupons().add(couponJpa);
        }

        for (OrderStatusHistory history : order.getStatusHistories()) {
            OrderStatusHistoryJpa historyJpa = orderMapper.toStatusHistoryJpa(history);
            historyJpa.setOrder(jpa);
            jpa.getStatusHistories().add(historyJpa);
        }

        return jpa;
    }

    private SubOrderJpa buildSubOrderJpa(SubOrder subOrder, OrderJpa orderJpa) {
        SubOrderJpa jpa = SubOrderJpa.builder()
                .sellerId(subOrder.getSellerId())
                .sellerName(subOrder.getSellerName())
                .status(subOrder.getStatus())
                .trackingNumber(subOrder.getTrackingNumber())
                .carrier(subOrder.getCarrier())
                .estimatedDelivery(subOrder.getEstimatedDelivery())
                .note(subOrder.getNote())
                .processedAt(subOrder.getProcessedAt())
                .shippedAt(subOrder.getShippedAt())
                .orderItems(new ArrayList<>())
                .build();

        if (subOrder.getId() != null) {
            jpa.setId(subOrder.getId());
        }
        jpa.setOrder(orderJpa);

        return jpa;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findByIdAndNotDeleted(UUID id) {
        return orderRepository.findByIdAndIsDeletedFalse(id)
                .map(orderMapper::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .map(orderMapper::toDomain);
    }

    @Override
    public List<Order> findAll(UUID buyerId, OrderStatus status, int page, int size) {
        return orderRepository.findAll(
            (root, query, cb) -> {
                var predicates = cb.and();
                if (buyerId != null) {
                    predicates = cb.and(predicates, cb.equal(root.get("buyerId"), buyerId));
                }
                if (status != null) {
                    predicates = cb.and(predicates, cb.equal(root.get("status"), status));
                }
                return predicates;
            },
            PageRequest.of(page, size)
        ).getContent().stream()
          .map(orderMapper::toDomain)
          .toList();
    }

    @Override
    public Order getById(UUID id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDomain)
                .orElse(null);
    }
}