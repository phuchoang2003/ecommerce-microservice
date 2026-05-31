package com.hdp.product_service.domain.model.valueobject;

public enum OutboxEventType {
    PRODUCT_CREATED("ProductCreatedDomainEvent"),
//    PRODUCT_UPDATED("ProductUpdated"),
    PRODUCT_DELETED("ProductDeleted");

    private final String eventType;

    OutboxEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public static OutboxEventType fromString(String value) {
        for (OutboxEventType type : values()) {
            if (type.eventType.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }
}