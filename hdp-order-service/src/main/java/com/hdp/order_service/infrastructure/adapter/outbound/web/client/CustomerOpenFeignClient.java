package com.hdp.order_service.infrastructure.adapter.outbound.web.client;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "customer-service", path = "/api/v1")
public interface CustomerOpenFeignClient {

    @GetMapping("/users/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable("id") UUID id);
}
