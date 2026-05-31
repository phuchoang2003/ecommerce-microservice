-- =====================================================
-- Outbox Events Table for Transactional Outbox Pattern
-- Stores domain events to be published to Kafka
-- =====================================================

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    message_key VARCHAR(255) NOT NULL,
    payload BYTEA NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    published_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for polling scheduler to fetch pending events efficiently
CREATE INDEX idx_outbox_events_status_created_at ON outbox_events(status, created_at);

-- Index for grouping events by message key
CREATE INDEX idx_outbox_events_message_key ON outbox_events(message_key);

-- Index for retry logic
CREATE INDEX idx_outbox_events_status_retry ON outbox_events(status, retry_count);
