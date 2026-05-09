package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CreateOrderUsecase extends Usecase<CreateOrderUsecase.Command, CreateOrderUsecase.Result> {

    record Command(
        UUID buyerId,
        UUID shippingAddressId,
        PaymentMethod paymentMethod,
        List<CreateOrderItemCommand> items,
        List<String> couponCodes
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.buyerId() == null) {
                result.add("buyerId", "Buyer ID is required");
            }
            if (input.shippingAddressId() == null) {
                result.add("shippingAddressId", "Shipping address ID is required");
            }
            if (input.paymentMethod() == null) {
                result.add("paymentMethod", "Payment method is required");
            }
            if (input.items() == null || input.items().isEmpty()) {
                result.add("items", "Order items cannot be empty");
            } else {
                for (int i = 0; i < input.items().size(); i++) {
                    input.items().get(i).validate(input.items().get(i)).throwIfInvalid();
                }
            }

            return result;
        }
    }

    record CreateOrderItemCommand(
        UUID sellerId,
        UUID productId,
        UUID variantId,
        int quantity
    ) implements SelfValidator<CreateOrderItemCommand> {

        @Override
        public ValidationResult validate(CreateOrderItemCommand input) {
            ValidationResult result = new ValidationResult();

            if (input.sellerId() == null) {
                result.add("sellerId", "Seller ID is required");
            }
            if (input.productId() == null) {
                result.add("productId", "Product ID is required");
            }
            if (input.variantId() == null) {
                result.add("variantId", "Variant ID is required");
            }
            if (input.quantity() <= 0) {
                result.add("quantity", "Quantity must be greater than zero");
            }

            return result;
        }
    }

    record Result(
        UUID id, String orderNumber, UUID buyerId, UUID shippingAddressId,
        PaymentMethod paymentMethod, OrderStatus status,
        BigDecimal subtotal, BigDecimal shippingFee, BigDecimal discount, BigDecimal tax,
        BigDecimal totalAmount, String paymentIntentId,
        Instant expiresAt, Instant paidAt, Instant cancelledAt, String cancellationReason,
        Instant createdAt, Instant updatedAt,
        List<SubOrderResult> subOrders,
        List<OrderItemResult> items,
        List<AppliedCouponResult> appliedCoupons
    ) {}

    record SubOrderResult(
        UUID id, UUID orderId, UUID sellerId, String sellerName,
        SubOrderStatus status, String trackingNumber, String carrier,
        LocalDate estimatedDelivery, String note, Instant processedAt, Instant shippedAt,
        int itemCount, Instant createdAt, Instant updatedAt
    ) {}

    record OrderItemResult(
        UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
        UUID productId, UUID variantId, String productName, String variantName,
        BigDecimal price, Integer quantity, BigDecimal subtotal
    ) {}

    record AppliedCouponResult(
        UUID id, UUID orderId, String code, String couponType, BigDecimal discountValue
    ) {}
}
