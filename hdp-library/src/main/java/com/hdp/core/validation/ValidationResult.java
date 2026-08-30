package com.hdp.core.validation;


import com.hdp.core.exception.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    private final List<ValidationError> errors = new ArrayList<>();

    public void add(String field, String message) {
        errors.add(new ValidationError(field, message));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    // =========================
    // THROW HELPER
    // =========================

    public void throwIfInvalid() {
        if (isValid()) return;

        Map<String, List<String>> details = new HashMap<>();

        for (ValidationError error : errors) {
            details
                    .computeIfAbsent(error.field(), k -> new ArrayList<>())
                    .add(error.message());
        }

        throw new ValidationException("Validation failed", (Map) details);
    }
}