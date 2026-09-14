package com.hdp.customer_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.customer_service.application.port.in.createuser.CreateUserCommand;
import com.hdp.customer_service.application.port.in.createuser.CreateUserResult;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdResult;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserCommand;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserResult;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.request.AddressRequest;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.request.CreateUserRequest;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.response.AddressResponse;
import com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.response.UserResponse;

import java.util.List;
import java.util.UUID;

public final class UserWebMapper {

    private UserWebMapper() {
    }

    public static CreateUserCommand toCreateUserCommand(CreateUserRequest request) {
        return new CreateUserCommand(
            request.fullName(),
            request.email(),
            request.phone(),
            request.dateOfBirth(),
            request.gender(),
            request.avatarUrl(),
            toAddressList(request.addresses())
        );
    }

    public static UpdateUserCommand toUpdateUserCommand(UUID id, CreateUserRequest request) {
        return new UpdateUserCommand(
            id,
            request.fullName(),
            request.email(),
            request.phone(),
            request.dateOfBirth(),
            request.gender(),
            request.avatarUrl(),
            toAddressList(request.addresses())
        );
    }

    public static UserResponse toResponse(CreateUserResult result) {
        return UserResponse.builder()
            .id(result.id())
            .fullName(result.fullName())
            .email(result.email())
            .phone(result.phone())
            .dateOfBirth(result.dateOfBirth())
            .gender(result.gender())
            .avatarUrl(result.avatarUrl())
            .addresses(toAddressResponseList(result.addresses()))
            .build();
    }

    public static UserResponse toResponse(UpdateUserResult result) {
        return UserResponse.builder()
            .id(result.id())
            .fullName(result.fullName())
            .email(result.email())
            .phone(result.phone())
            .dateOfBirth(result.dateOfBirth())
            .gender(result.gender())
            .avatarUrl(result.avatarUrl())
            .addresses(toAddressResponseList(result.addresses()))
            .build();
    }

    public static UserResponse toResponse(GetUserByIdResult result) {
        return UserResponse.builder()
            .id(result.id())
            .fullName(result.fullName())
            .email(result.email())
            .phone(result.phone())
            .dateOfBirth(result.dateOfBirth())
            .gender(result.gender())
            .avatarUrl(result.avatarUrl())
            .addresses(toAddressResponseList(result.addresses()))
            .build();
    }

    private static List<Address> toAddressList(List<AddressRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return List.of();
        }
        return requests.stream()
            .map(r -> new Address(null, r.street(), r.ward(), r.district(), r.city(), r.country()))
            .toList();
    }

    private static List<AddressResponse> toAddressResponseList(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return List.of();
        }
        return addresses.stream()
            .map(a -> AddressResponse.builder()
                .id(a.id())
                .street(a.street())
                .ward(a.ward())
                .district(a.district())
                .city(a.city())
                .country(a.country())
                .build())
            .toList();
    }
}
