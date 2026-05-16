package com.hdp.product_service.infrastructure.adapter.outbound.messaging.validating;

import com.hdp.common.messaging.validator.EventValidator;
import com.hdp.core.validation.Rule;
import com.hdp.core.validation.ValidationResult;
import com.hdp.messaging.event.product.ProductCreatedEventData;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductEventValidator implements EventValidator<ProductCreatedIntegrationEvent> {

    public ProductEventValidator() {
        log.info("ProductEventValidator created! supportedType={}", getSupportedEventType());
    }

    @Override
    public Class<ProductCreatedIntegrationEvent> getSupportedEventType() {
        return ProductCreatedIntegrationEvent.class;
    }

    @Override
    public void validate(ProductCreatedIntegrationEvent event) {
        log.debug("Validating ProductCreatedIntegrationEvent");

        ValidationResult result = new ValidationResult();
        ProductCreatedEventData data = event.getData();

        Rule.of(data.getName(), "name", result)
                .notBlank("Product name is required")
                .maxLength(255, "Product name must not exceed 255 characters");

        Rule.of(data.getPrice(), "price", result)
                .notNull("Price is required")
                .positive("Price must be greater than zero");

        result.throwIfInvalid();
        log.debug("ProductCreatedIntegrationEvent validation passed");
    }
}