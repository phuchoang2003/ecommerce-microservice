## 001. Use Outbox Pattern to Solve Dual Write Problem

**Date:** 2026-05-31

**Status:** Proposed

**Context:** In a microservice architecture, we need to update a database and publish domain events to Kafka as part of the same business operation. The challenge is:

- When updating the database succeeds but publishing to Kafka fails, the system enters an inconsistent state
- The domain entity is persisted but downstream services never receive the event
- This leads to data divergence between services that rely on eventual consistency

We evaluated several approaches:

- **Direct dual write** — Update DB and publish to Kafka in sequence. Risk of inconsistency if Kafka fails.
- **2PC (Two-Phase Commit)** — Not supported by Kafka; adds significant complexity.
- **Outbox Pattern** — Write to DB and outbox table in single transaction, then publish via background worker.

**Decision:** We will use the Outbox Pattern to solve the dual write problem.

The implementation will:
1. Add an `outbox_events` table alongside domain entities
2. Within the same DB transaction, insert both the domain entity AND an outbox event record
3. A background scheduler (or polling worker) reads unprocessed outbox events and publishes to Kafka
4. On successful publish, mark the outbox event as processed

**Consequences:**

**Benefits:**
- Atomicity guaranteed — if DB transaction commits, event is guaranteed to be in outbox
- Eventual delivery to Kafka with at-least-once semantics
- No data loss when Kafka is temporarily unavailable
- Works with existing transactional infrastructure (JPA/Flyway)

**Drawbacks:**
- Introduces additional latency — events are not published immediately
- Requires idempotency handling in consumers (due to potential duplicates)
- Additional storage overhead for the outbox table
- Complexity in the polling/publishing mechanism

**Alternatives Considered:**

| Approach | Why Not Chosen |
|----------|----------------|
| Polling publisher with DB logs | More complex to implement correctly |
| Transactional MQ send | Not supported by Kafka |
| Change Data Capture (CDC) | Adds infrastructure complexity (Debezium) |