package com.hdp.order_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderQuery;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderQueryHandler;
import com.hdp.order_service.application.port.in.getsuborder.GetSubOrderResult;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsQuery;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsQueryHandler;
import com.hdp.order_service.application.port.in.getsuborderitems.GetSubOrderItemsResult;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusCommand;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusCommandHandler;
import com.hdp.order_service.application.port.in.updatesuborderstatus.UpdateSubOrderStatusResult;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingCommand;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingCommandHandler;
import com.hdp.order_service.application.port.in.updatesubordertracking.UpdateSubOrderTrackingResult;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderStatusRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderTrackingRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderItemResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.mapper.SubOrderWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sub-orders")
@RequiredArgsConstructor
public class SubOrderController {

    private final GetSubOrderQueryHandler getSubOrderQueryHandler;
    private final UpdateSubOrderStatusCommandHandler updateSubOrderStatusCommandHandler;
    private final UpdateSubOrderTrackingCommandHandler updateSubOrderTrackingCommandHandler;
    private final GetSubOrderItemsQueryHandler getSubOrderItemsQueryHandler;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubOrderResponse>> getSubOrder(@PathVariable UUID id) {
        GetSubOrderResult result = getSubOrderQueryHandler.handle(new GetSubOrderQuery(id));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SubOrderResponse>> updateSubOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSubOrderStatusRequest request) {
        UpdateSubOrderStatusResult result = updateSubOrderStatusCommandHandler.handle(
            SubOrderWebMapper.toUpdateSubOrderStatusCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result), "SubOrder status updated"));
    }

    @PatchMapping("/{id}/tracking")
    public ResponseEntity<ApiResponse<SubOrderResponse>> updateSubOrderTracking(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSubOrderTrackingRequest request) {
        UpdateSubOrderTrackingResult result = updateSubOrderTrackingCommandHandler.handle(
            SubOrderWebMapper.toUpdateSubOrderTrackingCommand(id, request));
        return ResponseEntity.ok(ApiResponse.success(SubOrderWebMapper.toResponse(result), "SubOrder tracking updated"));
    }

    @GetMapping("/{subOrderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getSubOrderItems(@PathVariable UUID subOrderId) {
        GetSubOrderItemsResult result = getSubOrderItemsQueryHandler.handle(new GetSubOrderItemsQuery(subOrderId));
        List<OrderItemResponse> responses = result.items().stream()
            .map(SubOrderWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
