package com.hdp.order_service.application.eventhandler;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.messaging.event.product.ProductCreatedEventData;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductCreatedIntegrationEventHandler.
 *
 * <p>Test scenarios:
 * <ul>
 *   <li>Valid event - should save snapshot</li>
 *   <li>Duplicate event (idempotency) - DuplicateKeyBusinessException is swallowed</li>
 *   <li>Null data - should skip without saving</li>
 *   <li>Null/empty/invalid productId - should skip without saving</li>
 *   <li>Null name - should use "Unknown" default</li>
 *   <li>Null price - should use BigDecimal.ZERO default</li>
 * </ul>
 */
class ProductCreatedIntegrationEventHandlerTest {

    @Mock
    private ProductionSnapshotPersistencePort snapshotPersistencePort;

    private ProductCreatedIntegrationEventHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new ProductCreatedIntegrationEventHandler(snapshotPersistencePort);
    }

    @Test
    void handle_withValidEvent_shouldSaveSnapshot() {
        UUID productId = UUID.randomUUID();
        ProductCreatedIntegrationEvent record = createEvent(productId.toString(), "Test Product", new BigDecimal("99.99"));

        handler.handle(record, mock(Acknowledgment.class));

        ArgumentCaptor<ProductSnapshot> captor = ArgumentCaptor.forClass(ProductSnapshot.class);
        verify(snapshotPersistencePort).save(captor.capture());

        ProductSnapshot saved = captor.getValue();
        assertEquals(productId, saved.productId());
        assertEquals(productId, saved.variantId());
        assertEquals("Test Product", saved.productName());
        assertEquals("Test Product", saved.variantName());
        assertEquals(new BigDecimal("99.99"), saved.price());
    }

    @Test
    void handle_withDuplicateEvent_shouldSwallowDuplicateKey() {
        UUID productId = UUID.randomUUID();
        ProductCreatedIntegrationEvent record = createEvent(productId.toString(), "Test Product", new BigDecimal("99.99"));

        doThrow(new DuplicateKeyBusinessException("ProductSnapshot", productId + "/" + productId))
                .when(snapshotPersistencePort).save(any());

        assertDoesNotThrow(() -> handler.handle(record, mock(Acknowledgment.class)));

        verify(snapshotPersistencePort).save(any());
    }

    @Test
    void handle_withNullProductCreatedEventData_shouldSkipWithoutSaving() {
        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(null);

        handler.handle(record, mock(Acknowledgment.class));

        verify(snapshotPersistencePort, never()).save(any());
    }

    @Test
    void handle_withNullProductId_shouldSkipWithoutSaving() {
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn(null);

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);

        handler.handle(record, mock(Acknowledgment.class));

        verify(snapshotPersistencePort, never()).save(any());
    }

    @Test
    void handle_withEmptyProductId_shouldSkipWithoutSaving() {
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn("   ");

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);

        handler.handle(record, mock(Acknowledgment.class));

        verify(snapshotPersistencePort, never()).save(any());
    }

    @Test
    void handle_withInvalidUuidFormat_shouldSkipWithoutSaving() {
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn("not-a-uuid");

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);

        handler.handle(record, mock(Acknowledgment.class));

        verify(snapshotPersistencePort, never()).save(any());
    }

    @Test
    void handle_withNullName_shouldUseDefaultValue() {
        UUID productId = UUID.randomUUID();
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn(productId.toString());
        when(data.getName()).thenReturn(null);
        when(data.getPrice()).thenReturn(new BigDecimal("50.00"));

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);

        handler.handle(record, mock(Acknowledgment.class));

        ArgumentCaptor<ProductSnapshot> captor = ArgumentCaptor.forClass(ProductSnapshot.class);
        verify(snapshotPersistencePort).save(captor.capture());

        assertEquals("Unknown", captor.getValue().productName());
        assertEquals("Unknown", captor.getValue().variantName());
    }

    @Test
    void handle_withNullPrice_shouldUseDefaultValue() {
        UUID productId = UUID.randomUUID();
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn(productId.toString());
        when(data.getName()).thenReturn("Test Product");
        when(data.getPrice()).thenReturn(null);

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);

        handler.handle(record, mock(Acknowledgment.class));

        ArgumentCaptor<ProductSnapshot> captor = ArgumentCaptor.forClass(ProductSnapshot.class);
        verify(snapshotPersistencePort).save(captor.capture());

        assertEquals(BigDecimal.ZERO, captor.getValue().price());
    }

    private ProductCreatedIntegrationEvent createEvent(String productId, String name, BigDecimal price) {
        ProductCreatedEventData data = mock(ProductCreatedEventData.class);
        when(data.getProductId()).thenReturn(productId);
        when(data.getName()).thenReturn(name);
        when(data.getPrice()).thenReturn(price);

        ProductCreatedIntegrationEvent record = mock(ProductCreatedIntegrationEvent.class);
        when(record.getData()).thenReturn(data);
        return record;
    }
}
