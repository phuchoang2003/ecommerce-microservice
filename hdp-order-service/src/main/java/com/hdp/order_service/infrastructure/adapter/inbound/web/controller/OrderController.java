package com.hdp.order_service.infrastructure.adapter.inbound.web.controller;



import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.common.web.dto.response.PagedResponse;
import com.hdp.order_service.application.port.in.CancelOrderUsecase;
import com.hdp.order_service.application.port.in.CreateOrderUsecase;
import com.hdp.order_service.application.port.in.GetAppliedCouponsUsecase;
import com.hdp.order_service.application.port.in.GetOrderHistoryUsecase;
import com.hdp.order_service.application.port.in.GetOrderItemsUsecase;
import com.hdp.order_service.application.port.in.GetOrderUsecase;
import com.hdp.order_service.application.port.in.ListOrdersUsecase;
import com.hdp.order_service.application.port.in.ListSubOrdersByOrderUsecase;
import com.hdp.order_service.application.port.in.UpdateOrderStatusUsecase;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.CancelOrderRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.CreateOrderRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateOrderStatusRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.AppliedCouponResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderItemResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderStatusHistoryResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderSummaryResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderSummaryResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.mapper.OrderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUsecase createOrderUsecase;
    private final GetOrderUsecase getOrderUsecase;
    private final ListOrdersUsecase listOrdersUsecase;
    private final UpdateOrderStatusUsecase updateOrderStatusUsecase;
    private final CancelOrderUsecase cancelOrderUsecase;
    private final GetOrderHistoryUsecase getOrderHistoryUsecase;
    private final ListSubOrdersByOrderUsecase listSubOrdersByOrderUsecase;
    private final GetOrderItemsUsecase getOrderItemsUsecase;
    private final GetAppliedCouponsUsecase getAppliedCouponsUsecase;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        CreateOrderUsecase.Result result = createOrderUsecase.execute(OrderWebMapper.toCreateOrderCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(OrderWebMapper.toResponse(result), "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable UUID id) {
        GetOrderUsecase.Result result = getOrderUsecase.execute(OrderWebMapper.toGetOrderQuery(id));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result)));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<OrderSummaryResponse>> listOrders(
            @RequestParam(required = false) UUID buyerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        ListOrdersUsecase.Result result = listOrdersUsecase.execute(
            OrderWebMapper.toListOrdersQuery(buyerId, status, page, size));
        List<OrderSummaryResponse> responses = result.orderResults().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(responses, page, size, responses.size()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        UpdateOrderStatusUsecase.Result result = updateOrderStatusUsecase.execute(
            OrderWebMapper.toUpdateOrderStatusCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result), "Order status updated"));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CancelOrderRequest request) {
        CancelOrderUsecase.Result result = cancelOrderUsecase.execute(
            OrderWebMapper.toCancelOrderCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result), "Order cancelled"));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<OrderStatusHistoryResponse>>> getOrderHistory(@PathVariable UUID id) {
        GetOrderHistoryUsecase.Result result = getOrderHistoryUsecase.execute(OrderWebMapper.toGetOrderHistoryQuery(id));
        List<OrderStatusHistoryResponse> responses = result.histories().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/sub-orders")
    public ResponseEntity<ApiResponse<List<SubOrderSummaryResponse>>> listSubOrders(@PathVariable UUID orderId) {
        ListSubOrdersByOrderUsecase.Result result = listSubOrdersByOrderUsecase.execute(
            OrderWebMapper.toListSubOrdersQuery(orderId));
        List<SubOrderSummaryResponse> responses = result.subOrderResults().stream()
            .map(OrderWebMapper::toSubOrderSummaryResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getOrderItems(@PathVariable UUID orderId) {
        GetOrderItemsUsecase.Result result = getOrderItemsUsecase.execute(
            OrderWebMapper.toGetOrderItemsQuery(orderId));
        List<OrderItemResponse> responses = result.items().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/coupons")
    public ResponseEntity<ApiResponse<List<AppliedCouponResponse>>> getAppliedCoupons(@PathVariable UUID orderId) {
        GetAppliedCouponsUsecase.Result result = getAppliedCouponsUsecase.execute(
            OrderWebMapper.toGetAppliedCouponsQuery(orderId));
        List<AppliedCouponResponse> responses = result.appliedCoupons().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
