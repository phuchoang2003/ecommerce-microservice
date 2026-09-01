package com.hdp.order_service.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * Base for full-stack integration tests of the order service.
 *
 * <p>Boots the real {@code @SpringBootApplication} context against a fresh
 * Testcontainers Postgres. The Kafka listener is disabled via the {@code test}
 * profile ({@code spring.kafka.listener.auto-startup: false}), so this base
 * does NOT need a Kafka broker. Tests that need Kafka transport should add
 * an {@code EmbeddedKafkaBroker} or Confluent Testcontainers on top of this.
 *
 * <p>Tests should construct real Avro events and call the
 * {@code ProductCreatedIntegrationEventHandler} bean directly. This exercises:
 * Spring DI, the JPA adapter, Hibernate,
 * Flyway migrations, and the real Postgres DB.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractPostgresIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
                    .withDatabaseName("order_db")
                    .withUsername("hdp_user")
                    .withPassword("postgres")
                    .withReuse(true);

    @DynamicPropertySource
    static void registerDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
