package com.hdp.order_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.order_service.application.port.in.*;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderStatusRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderTrackingRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderItemResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.mapper.SubOrderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sub-orders")
@RequiredArgsConstructor
public class SubOrderController {

    private final GetSubOrderUsecase getSubOrderUsecase;
    private final UpdateSubOrderStatusUsecase updateSubOrderStatusUsecase;
    private final UpdateSubOrderTrackingUsecase updateSubOrderTrackingUsecase;
    private final GetSubOrderItemsUsecase getSubOrderItemsUsecase;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubOrderResponse>> getSubOrder(@PathVariable UUID id) {
        GetSubOrderUsecase.Result result = getSubOrderUsecase.execute(SubOrderWebMapper.toGetSubOrderQuery(id));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SubOrderResponse>> updateSubOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSubOrderStatusRequest request) {
        UpdateSubOrderStatusUsecase.Result result = updateSubOrderStatusUsecase.execute(
            SubOrderWebMapper.toUpdateSubOrderStatusCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result), "SubOrder status updated"));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<ApiResponse<SubOrderResponse>> updateSubOrderTracking(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSubOrderTrackingRequest request) {
        UpdateSubOrderTrackingUsecase.Result result = updateSubOrderTrackingUsecase.execute(
            SubOrderWebMapper.toUpdateSubOrderTrackingCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result), "SubOrder tracking updated"));
    }

    @GetMapping("/{subOrderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getSubOrderItems(@PathVariable UUID subOrderId) {
        GetSubOrderItemsUsecase.Result result = getSubOrderItemsUsecase.execute(
            SubOrderWebMapper.toGetSubOrderItemsQuery(subOrderId));
        List<OrderItemResponse> responses = result.items().stream()
            .map(SubOrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
