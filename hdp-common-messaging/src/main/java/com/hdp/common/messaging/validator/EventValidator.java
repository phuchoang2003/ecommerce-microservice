package com.hdp.common.messaging.validator;

import org.apache.avro.specific.SpecificRecord;

public interface EventValidator<T extends SpecificRecord> {
    Class<?> getSupportedEventType();
    void validate(T event);
}