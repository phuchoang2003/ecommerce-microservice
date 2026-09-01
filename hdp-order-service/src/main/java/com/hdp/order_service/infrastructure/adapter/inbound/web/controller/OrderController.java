package com.hdp.order_service.infrastructure.adapter.inbound.web.controller;


import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.common.web.dto.response.PagedResponse;
import com.hdp.order_service.application.port.in.cancelorder.CancelOrderCommandHandler;
import com.hdp.order_service.application.port.in.cancelorder.CancelOrderResult;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommandHandler;
import com.hdp.order_service.application.port.in.createorder.CreateOrderResult;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsQuery;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsQueryHandler;
import com.hdp.order_service.application.port.in.getappliedcoupons.GetAppliedCouponsResult;
import com.hdp.order_service.application.port.in.getorder.GetOrderQuery;
import com.hdp.order_service.application.port.in.getorder.GetOrderQueryHandler;
import com.hdp.order_service.application.port.in.getorder.GetOrderResult;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryQuery;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryQueryHandler;
import com.hdp.order_service.application.port.in.getorderhistory.GetOrderHistoryResult;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsQuery;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsQueryHandler;
import com.hdp.order_service.application.port.in.getorderitems.GetOrderItemsResult;
import com.hdp.order_service.application.port.in.listorders.ListOrdersQuery;
import com.hdp.order_service.application.port.in.listorders.ListOrdersQueryHandler;
import com.hdp.order_service.application.port.in.listorders.ListOrdersResult;
import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderQuery;
import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderQueryHandler;
import com.hdp.order_service.application.port.in.listsubordersbyorder.ListSubOrdersByOrderResult;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusCommandHandler;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusResult;
import com.hdp.order_service.domain.valueobject.OrderStatus;
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

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final GetOrderQueryHandler getOrderQueryHandler;
    private final ListOrdersQueryHandler listOrdersQueryHandler;
    private final UpdateOrderStatusCommandHandler updateOrderStatusCommandHandler;
    private final CancelOrderCommandHandler cancelOrderCommandHandler;
    private final GetOrderHistoryQueryHandler getOrderHistoryQueryHandler;
    private final ListSubOrdersByOrderQueryHandler listSubOrdersByOrderQueryHandler;
    private final GetOrderItemsQueryHandler getOrderItemsQueryHandler;
    private final GetAppliedCouponsQueryHandler getAppliedCouponsQueryHandler;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResult result = createOrderCommandHandler.handle(OrderWebMapper.toCreateOrderCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(OrderWebMapper.toResponse(result), "Order created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable UUID id) {
        GetOrderResult result = getOrderQueryHandler.handle(new GetOrderQuery(id));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result)));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<OrderSummaryResponse>> listOrders(
            @RequestParam(required = false) UUID buyerId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        ListOrdersResult result = listOrdersQueryHandler.handle(new ListOrdersQuery(buyerId, status, page, size));
        List<OrderSummaryResponse> responses = result.orderResults().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(responses, page, size, responses.size()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        UpdateOrderStatusResult result = updateOrderStatusCommandHandler.handle(
            OrderWebMapper.toUpdateOrderStatusCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result), "Order status updated"));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CancelOrderRequest request) {
        CancelOrderResult result = cancelOrderCommandHandler.handle(
            OrderWebMapper.toCancelOrderCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(OrderWebMapper.toResponse(result), "Order cancelled"));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<ApiResponse<List<OrderStatusHistoryResponse>>> getOrderHistory(@PathVariable UUID id) {
        GetOrderHistoryResult result = getOrderHistoryQueryHandler.handle(new GetOrderHistoryQuery(id));
        List<OrderStatusHistoryResponse> responses = result.histories().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/sub-orders")
    public ResponseEntity<ApiResponse<List<SubOrderSummaryResponse>>> listSubOrders(@PathVariable UUID orderId) {
        ListSubOrdersByOrderResult result = listSubOrdersByOrderQueryHandler.handle(
            new ListSubOrdersByOrderQuery(orderId));
        List<SubOrderSummaryResponse> responses = result.subOrderResults().stream()
            .map(OrderWebMapper::toSubOrderSummaryResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getOrderItems(@PathVariable UUID orderId) {
        GetOrderItemsResult result = getOrderItemsQueryHandler.handle(new GetOrderItemsQuery(orderId));
        List<OrderItemResponse> responses = result.items().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{orderId}/coupons")
    public ResponseEntity<ApiResponse<List<AppliedCouponResponse>>> getAppliedCoupons(@PathVariable UUID orderId) {
        GetAppliedCouponsResult result = getAppliedCouponsQueryHandler.handle(new GetAppliedCouponsQuery(orderId));
        List<AppliedCouponResponse> responses = result.appliedCoupons().stream()
            .map(OrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
