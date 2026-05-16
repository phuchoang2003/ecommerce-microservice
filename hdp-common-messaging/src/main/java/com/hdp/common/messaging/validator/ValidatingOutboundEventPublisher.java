package com.hdp.common.messaging.validator;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ValidatingOutboundEventPublisher implements OutboundEventPublisher {

    private final OutboundEventPublisher delegate;
    private final Map<String, EventValidator<?>> validatorMap;

    public ValidatingOutboundEventPublisher(
            OutboundEventPublisher delegate,
            List<EventValidator<?>> validators) {
        this.delegate = delegate;
        this.validatorMap = validators.stream()
                .collect(Collectors.toMap(
                        v -> v.getSupportedEventType().getName(),  // Use class name as key
                        v -> v,
                        (v1, v2) -> v1
                ));
        log.info("ValidatingOutboundEventPublisher initialized with {} validators: {}",
                validators.size(), validatorMap.keySet());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void send(Object event, String topic, String key) {
        String eventClassName = event.getClass().getName();
        log.info("send() called - eventClassName: {}, validatorMap keys: {}",
                eventClassName, validatorMap.keySet());

        EventValidator<?> validator = validatorMap.get(eventClassName);
        if (validator != null) {
            log.info("Validating event type: {}", event.getClass().getSimpleName());
            ((EventValidator<SpecificRecord>) validator).validate((SpecificRecord) event);
        } else {
            log.warn("No validator found for event type: {}, skipping validation", eventClassName);
        }
        delegate.send(event, topic, key);
    }
}