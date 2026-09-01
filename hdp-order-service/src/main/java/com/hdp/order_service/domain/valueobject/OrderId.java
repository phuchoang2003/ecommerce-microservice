package com.hdp.order_service.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record OrderId(UUID value) {
    public OrderId {
        Objects.requireNonNull(
                value,
                "OrderId cannot be null"
        );
    }

    public static OrderId generate(){
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(UUID value){
        return new OrderId(value);
    }
}
