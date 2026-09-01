package com.hdp.product_service.application.port.in.deleteproduct;

import java.util.UUID;

public record DeleteProductCommand(UUID id, UUID sellerId) {
}
