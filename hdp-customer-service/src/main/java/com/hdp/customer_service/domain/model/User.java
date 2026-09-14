package com.hdp.customer_service.domain.model;

import com.hdp.core.exception.BusinessException;
import com.hdp.core.model.AggregateRoot;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;
import com.hdp.customer_service.domain.valueobject.UserId;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class User extends AggregateRoot<UserId> {

    private static final int FULL_NAME_MAX = 255;
    private static final int EMAIL_MAX = 255;
    private static final int PHONE_MAX = 20;
    private static final int AVATAR_URL_MAX = 500;

    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String avatarUrl;
    private List<Address> addresses;

    private User(UserId id, String fullName, String email, String phone,
                 LocalDate dateOfBirth, Gender gender, String avatarUrl,
                 List<Address> addresses) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.addresses = addresses != null ? new ArrayList<>(addresses) : new ArrayList<>();
    }

    public static User create(String fullName, String email, String phone,
                              LocalDate dateOfBirth, Gender gender, String avatarUrl,
                              List<Address> addresses) {
        validateProfile(fullName, email, phone, avatarUrl);
        Instant now = Instant.now();
        return new User(
                UserId.generate(),
                fullName, email, phone, dateOfBirth, gender, avatarUrl,
                copyAddresses(addresses)
        );
    }

    public static User reconstitute(UserId id, String fullName, String email, String phone,
                                    LocalDate dateOfBirth, Gender gender, String avatarUrl,
                                    List<Address> addresses) {
        return new User(id, fullName, email, phone, dateOfBirth, gender, avatarUrl,
                addresses);
    }

    public void update(String fullName, String email, String phone,
                       LocalDate dateOfBirth, Gender gender, String avatarUrl,
                       List<Address> addresses) {
        validateProfile(fullName, email, phone, avatarUrl);
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.avatarUrl = avatarUrl;
        this.addresses = copyAddresses(addresses);
    }


    public List<Address> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    private static void validateProfile(String fullName, String email, String phone, String avatarUrl) {
        requireText("fullName", fullName, FULL_NAME_MAX);
        requireText("email", email, EMAIL_MAX);
        requireOptionalText("phone", phone, PHONE_MAX);
        requireOptionalText("avatarUrl", avatarUrl, AVATAR_URL_MAX);
    }

    private static void requireText(String field, String value, int max) {
        if (value == null || value.isBlank()) {
            throw new BusinessException("USER_FIELD_REQUIRED", field);
        }
        if (value.length() > max) {
            throw new BusinessException("USER_FIELD_TOO_LONG", field, max);
        }
    }

    private static void requireOptionalText(String field, String value, int max) {
        if (value == null) {
            return;
        }
        if (value.isBlank()) {
            throw new BusinessException("USER_FIELD_BLANK", field);
        }
        if (value.length() > max) {
            throw new BusinessException("USER_FIELD_TOO_LONG", field, max);
        }
    }

    private static List<Address> copyAddresses(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>();
        }
        // Re-validate every address on assignment so an invalid one never
        // sneaks in through update(). Address's own constructor throws on
        // invalid input, so this loop is the validation.
        List<Address> copy = new ArrayList<>(addresses.size());
        for (Address a : addresses) {
            copy.add(new Address(a.id(), a.street(), a.ward(), a.district(), a.city(), a.country()));
        }
        return copy;
    }
}
