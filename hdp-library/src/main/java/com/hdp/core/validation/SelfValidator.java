package com.hdp.core.validation;

public interface SelfValidator<T> {
    ValidationResult validate(T input);
}