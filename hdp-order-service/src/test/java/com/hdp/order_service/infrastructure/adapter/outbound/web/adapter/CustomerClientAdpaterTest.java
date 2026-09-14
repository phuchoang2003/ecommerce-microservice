package com.hdp.order_service.infrastructure.adapter.outbound.web.adapter;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.infrastructure.adapter.outbound.web.client.CustomerOpenFeignClient;
import com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response.AddressResponse;
import com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response.UserResponse;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerClientAdpaterTest {

    @Mock
    private CustomerOpenFeignClient feignClient;

    @InjectMocks
    private CustomerClientAdpater adapter;

    @Test
    void findAddressIdsByBuyerId_returnsAddressIds() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UserResponse user = new UserResponse(buyerId, List.of(
            new AddressResponse(addressId, "123 Le Loi", null, null, "HCMC", "VN")
        ));
        when(feignClient.getUser(buyerId)).thenReturn(ApiResponse.success(user, "ok"));

        assertThat(adapter.findAddressIdsByBuyerId(buyerId)).containsExactly(addressId);
    }

    @Test
    void findAddressIdsByBuyerId_returnsEmptyWhenUserHasNoAddresses() {
        UUID buyerId = UUID.randomUUID();
        when(feignClient.getUser(buyerId))
            .thenReturn(ApiResponse.success(new UserResponse(buyerId, List.of()), "ok"));

        assertThat(adapter.findAddressIdsByBuyerId(buyerId)).isEmpty();
    }

    @Test
    void findAddressIdsByBuyerId_returnsEmptyWhenAddressesNull() {
        UUID buyerId = UUID.randomUUID();
        when(feignClient.getUser(buyerId))
            .thenReturn(ApiResponse.success(new UserResponse(buyerId, null), "ok"));

        assertThat(adapter.findAddressIdsByBuyerId(buyerId)).isEmpty();
    }

    @Test
    void findAddressIdsByBuyerId_throwsNotFoundWhenFeignReturns404() {
        UUID buyerId = UUID.randomUUID();
        when(feignClient.getUser(buyerId)).thenThrow(notFound());

        assertThatThrownBy(() -> adapter.findAddressIdsByBuyerId(buyerId))
            .isInstanceOf(NotFoundException.class);
    }

    private static FeignException.NotFound notFound() {
        Response response = Response.builder()
            .status(404)
            .reason("Not Found")
            .request(Request.create(Request.HttpMethod.GET, "/api/v1/users/x", Map.of(), null, null, null))
            .body("{\"errorCode\":\"USER_NOT_FOUND\"}", StandardCharsets.UTF_8)
            .build();
        return (FeignException.NotFound) FeignException.errorStatus("CustomerOpenFeignClient#getUser", response);
    }
}
