package com.hdp.customer_service.application.port.in.createuser;

import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;

import java.time.LocalDate;
import java.util.List;

public record CreateUserCommand(
        String fullName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        Gender gender,
        String avatarUrl,
        List<Address> addresses
) {
}
