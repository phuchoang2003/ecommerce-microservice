package com.hdp.customer_service.infrastructure.adapter.outbound.persistence.mapper;

import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.UserId;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserAddressJpa;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserJpa;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public User toDomain(UserJpa jpa) {
        if (jpa == null) {
            return null;
        }
        return User.reconstitute(
            UserId.of(jpa.getId()),
            jpa.getFullName(),
            jpa.getEmail(),
            jpa.getPhone(),
            jpa.getDateOfBirth(),
            jpa.getGender(),
            jpa.getAvatarUrl(),
            toAddressList(jpa.getAddresses())
        );
    }

    public void applyToJpa(User user, UserJpa jpa) {
        jpa.setFullName(user.getFullName());
        jpa.setEmail(user.getEmail());
        jpa.setPhone(user.getPhone());
        jpa.setDateOfBirth(user.getDateOfBirth());
        jpa.setGender(user.getGender());
        jpa.setAvatarUrl(user.getAvatarUrl());

        replaceAddresses(jpa, user.getAddresses());
    }

    private List<Address> toAddressList(List<UserAddressJpa> jpaAddresses) {
        if (jpaAddresses == null || jpaAddresses.isEmpty()) {
            return new ArrayList<>();
        }
        List<Address> result = new ArrayList<>(jpaAddresses.size());
        for (UserAddressJpa jpa : jpaAddresses) {
            result.add(new Address(
                jpa.getId(),
                jpa.getStreet(),
                jpa.getWard(),
                jpa.getDistrict(),
                jpa.getCity(),
                jpa.getCountry()
            ));
        }
        return result;
    }

    private void replaceAddresses(UserJpa jpa, List<Address> addresses) {
        jpa.getAddresses().clear();
        if (addresses == null || addresses.isEmpty()) {
            return;
        }
        for (Address a : addresses) {
            UserAddressJpa addressJpa = UserAddressJpa.builder()
                .user(jpa)
                .street(a.street())
                .ward(a.ward())
                .district(a.district())
                .city(a.city())
                .country(a.country())
                .build();
            jpa.getAddresses().add(addressJpa);
        }
    }
}
