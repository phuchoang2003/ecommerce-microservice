package com.hdp.product_service.domain.model.valueobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class Money {
    private final BigDecimal amount;

    public static Money of(BigDecimal amount) {
        return new Money(amount != null ? amount : BigDecimal.ZERO);
    }

    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(this.amount.subtract(other.amount));
    }
}