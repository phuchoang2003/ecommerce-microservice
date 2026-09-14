package com.hdp.order_service.infrastructure.adapter.outbound.web.adapter;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.out.CustomerClientPort;
import com.hdp.order_service.infrastructure.adapter.outbound.web.client.CustomerOpenFeignClient;
import com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response.AddressResponse;
import com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response.UserResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerClientAdpater implements CustomerClientPort {

    private final CustomerOpenFeignClient customerOpenFeignClient;

    @Override
    public List<UUID> findAddressIdsByBuyerId(UUID buyerId) {
        UserResponse user = fetchUser(buyerId);
        if (user.addresses() == null) {
            return List.of();
        }
        return user.addresses().stream()
            .map(AddressResponse::id)
            .toList();
    }

    private UserResponse fetchUser(UUID buyerId) {
        try {
            ApiResponse<UserResponse> response = customerOpenFeignClient.getUser(buyerId);
            if (response == null || response.data() == null) {
                throw new NotFoundException("User", buyerId);
            }
            return response.data();
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("User", buyerId);
        }
    }
}
