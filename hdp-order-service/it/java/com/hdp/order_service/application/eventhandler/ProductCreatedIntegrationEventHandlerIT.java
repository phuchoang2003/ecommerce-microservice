package com.hdp.order_service.application.eventhandler;

import com.hdp.messaging.event.product.ProductCreatedEventData;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductSnapshotJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.ProductSnapshotRepositoryJpa;
import com.hdp.order_service.support.AbstractPostgresIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Full-stack integration test for the consumer-side dedup of
 * {@link ProductCreatedIntegrationEvent}.
 *
 * <p>Boots the real {@code @SpringBootApplication} against a Testcontainers
 * Postgres. Drives the {@link ProductCreatedIntegrationEventHandler}
 * directly with real Avro {@code ProductCreatedIntegrationEvent} instances,
 * which is the same code path the {@code @KafkaListener} invokes.
 *
 * <p>Kafka transport is bypassed: the listener is disabled via
 * {@code spring.kafka.listener.auto-startup=false} in
 * {@code it/resources/application-test.yaml}. To extend this to a true
 * end-to-end test, add a Confluent Schema Registry Testcontainer and
 * publish through {@code KafkaTemplate}.
 */

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class ProductCreatedIntegrationEventHandlerIT extends AbstractPostgresIntegrationTest {

    @Autowired
    ProductCreatedIntegrationEventHandler handler;

    @Autowired
    ProductSnapshotRepositoryJpa repository;

    @AfterEach
    void cleanSnapshotTable() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Single ProductCreated event persists exactly one snapshot row")
    void singleEvent_persistsOneRow() {
        UUID productId = UUID.randomUUID();

        handler.handle(buildEvent(productId, "Widget", new BigDecimal("9.99")), mock(Acknowledgment.class));

        List<ProductSnapshotJpa> rows = repository.findByProductIdIn(List.of(productId));
        assertThat(rows).hasSize(1);
        assertThat(rows.get(0).getProductName()).isEqualTo("Widget");
        assertThat(rows.get(0).getPrice()).isEqualByComparingTo("9.99");
    }

    @Test
    @DisplayName("Duplicate ProductCreated event for same productId is a no-op (idempotency)")
    void duplicateEvent_isIdempotent() {
        UUID productId = UUID.randomUUID();
        ProductCreatedIntegrationEvent event =
                buildEvent(productId, "Widget", new BigDecimal("9.99"));

        handler.handle(event, mock(Acknowledgment.class));
        handler.handle(event, mock(Acknowledgment.class));
        handler.handle(event, mock(Acknowledgment.class));

        assertThat(repository.findByProductIdIn(List.of(productId)))
                .as("duplicate dispatches must not produce duplicate rows")
                .hasSize(1);
    }

    @Test
    @DisplayName("Events with different productIds each create a distinct row")
    void differentProductIds_createDistinctRows() {
        UUID a = UUID.randomUUID();
        UUID b = UUID.randomUUID();

        handler.handle(buildEvent(a, "Alpha", new BigDecimal("1.00")), mock(Acknowledgment.class));
        handler.handle(buildEvent(b, "Beta",  new BigDecimal("2.00")), mock(Acknowledgment.class));

        assertThat(repository.findByProductIdIn(List.of(a, b)))
                .extracting(ProductSnapshotJpa::getProductName)
                .containsExactlyInAnyOrder("Alpha", "Beta");
    }

    @Test
    @DisplayName("Concurrent duplicate deliveries are deduped via UNIQUE constraint + DuplicateKeyBusinessException")
    void concurrentDuplicates_exposeRace() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductCreatedIntegrationEvent event =
                buildEvent(productId, "Widget", new BigDecimal("9.99"));

        int threads = 16;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                start.await();
                handler.handle(event, mock(Acknowledgment.class));
                return null;
            }));
        }
        start.countDown();
        for (Future<?> f : futures) {
            f.get(30, TimeUnit.SECONDS);
        }
        pool.shutdown();

        long count = repository.findByProductIdIn(List.of(productId)).size();
        // UNIQUE constraint on (product_id, variant_id) + handler catching
        // DuplicateKeyBusinessException guarantees exactly one row under
        // concurrent duplicate deliveries.
        assertThat(count).isEqualTo(1);
    }

    private ProductCreatedIntegrationEvent buildEvent(UUID productId, String name, BigDecimal price) {
        ProductCreatedEventData data = ProductCreatedEventData.newBuilder()
                .setProductId(productId.toString())
                .setSellerId(UUID.randomUUID().toString())
                .setName(name)
                .setPrice(price)
                .setImages(List.of())
                .setStatus("ACTIVE")
                .build();

        return ProductCreatedIntegrationEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType("ProductCreated")
                .setVersion(1)
                .setSource("test")
                .setCorrelationId(productId.toString())
                .setOccurredAt(System.currentTimeMillis())
                .setData(data)
                .build();
    }
}
