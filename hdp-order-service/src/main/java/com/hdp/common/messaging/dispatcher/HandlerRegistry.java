package com.hdp.common.messaging.dispatcher;

import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HandlerRegistry {

    private final Map<Class<?>, AvroEventHandler<?>> handlerMap = new HashMap<>();

    public HandlerRegistry(List<AvroEventHandler<?>> handlers) {
        for (AvroEventHandler<?> handler : handlers) {

            Class<?> eventType = resolveEventType(handler);

            if (eventType == null) {
                throw new RuntimeException(
                        "Cannot resolve generic type for " + handler.getClass()
                );
            }

            handlerMap.put(eventType, handler);
        }
    }

    private Class<?> resolveEventType(Object handler) {
        ResolvableType type = ResolvableType.forClass(handler.getClass())
                .as(AvroEventHandler.class);

        return type.getGeneric(0).resolve();
    }

    public AvroEventHandler<?> get(Class<?> clazz) {
        return handlerMap.get(clazz);
    }
}
