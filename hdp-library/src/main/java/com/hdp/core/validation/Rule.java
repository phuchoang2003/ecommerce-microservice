package com.hdp.core.validation;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Rule<T> {

    private final T value;
    private final String field;
    private final ValidationResult result;

    private Rule(T value, String field, ValidationResult result) {
        this.value = value;
        this.field = field;
        this.result = result;
    }

    public static <T> Rule<T> of(T value, String field, ValidationResult result) {
        return new Rule<>(value, field, result);
    }

    // =========================
    // NULL / EMPTY
    // =========================

    public Rule<T> notNull(String message) {
        if (value == null) result.add(field, message);
        return this;
    }

    public Rule<String> notBlank(String message) {
        if (value == null || ((String) value).trim().isEmpty()) {
            result.add(field, message);
        }
        return (Rule<String>) this;
    }

    public Rule<Collection<?>> notEmpty(String message) {
        if (value == null || ((Collection<?>) value).isEmpty()) {
            result.add(field, message);
        }
        return (Rule<Collection<?>>) this;
    }

    // =========================
    // SIZE / LENGTH
    // =========================

    public Rule<String> minLength(int min, String message) {
        if (value != null && ((String) value).length() < min) {
            result.add(field, message);
        }
        return (Rule<String>) this;
    }

    public Rule<String> maxLength(int max, String message) {
        if (value != null && ((String) value).length() > max) {
            result.add(field, message);
        }
        return (Rule<String>) this;
    }

    public Rule<Collection<?>> maxSize(int max, String message) {
        if (value != null && ((Collection<?>) value).size() > max) {
            result.add(field, message);
        }
        return (Rule<Collection<?>>) this;
    }

    // =========================
    // NUMBER
    // =========================

    public Rule<Integer> greaterThan(int min, String message) {
        if (value != null && ((Integer) value) <= min) {
            result.add(field, message);
        }
        return (Rule<Integer>) this;
    }

    public Rule<Integer> greaterOrEqual(int min, String message) {
        if (value != null && ((Integer) value) < min) {
            result.add(field, message);
        }
        return (Rule<Integer>) this;
    }

    public Rule<Integer> between(int min, int max, String message) {
        if (value != null) {
            int v = (Integer) value;
            if (v < min || v > max) {
                result.add(field, message);
            }
        }
        return (Rule<Integer>) this;
    }

    public Rule<BigDecimal> positive(String message) {
        if (value != null && ((BigDecimal) value).compareTo(BigDecimal.ZERO) <= 0) {
            result.add(field, message);
        }
        return (Rule<BigDecimal>) this;
    }

    // =========================
    // STRING / FORMAT
    // =========================

    public Rule<String> matches(String regex, String message) {
        if (value != null && !((String) value).matches(regex)) {
            result.add(field, message);
        }
        return (Rule<String>) this;
    }

    // =========================
    // ENUM / ALLOWED VALUES
    // =========================

    public Rule<T> in(Set<T> allowed, String message) {
        if (value != null && !allowed.contains(value)) {
            result.add(field, message);
        }
        return this;
    }

    // =========================
    // CONDITIONAL
    // =========================

    public Rule<T> when(boolean condition, Consumer<Rule<T>> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }

    // =========================
    // CUSTOM
    // =========================

    public Rule<T> must(Predicate<T> predicate, String message) {
        if (value != null && !predicate.test(value)) {
            result.add(field, message);
        }
        return this;
    }
}