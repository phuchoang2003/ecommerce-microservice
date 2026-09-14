package com.hdp.customer_service.application.port.in.updateuser;

import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record UpdateUserResult(
        UUID id,
        String fullName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String avatarUrl,
        List<Address> addresses
) {
}
