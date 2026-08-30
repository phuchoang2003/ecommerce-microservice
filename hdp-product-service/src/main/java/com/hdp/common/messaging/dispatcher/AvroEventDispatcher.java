package com.hdp.common.messaging.dispatcher;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

@Component
public class AvroEventDispatcher {

    private final HandlerRegistry registry;

    public AvroEventDispatcher(HandlerRegistry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    public void dispatch(SpecificRecord event) {

        AvroEventHandler handler = registry.get(event.getClass());

        if (handler == null) {
            throw new RuntimeException(
                    "No handler for: " + event.getClass().getName()
            );
        }

        handler.handle(event);
    }
}
