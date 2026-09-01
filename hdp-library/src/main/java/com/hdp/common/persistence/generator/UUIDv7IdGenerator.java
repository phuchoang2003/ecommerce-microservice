package com.hdp.common.persistence.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.uuid.UuidValueGenerator;

import java.security.SecureRandom;
import java.util.UUID;

public class UUIDv7IdGenerator implements UuidValueGenerator {
    @Override
    public UUID generateUuid(SharedSessionContractImplementor sharedSessionContractImplementor) {
        return generateV7();
    }

    private UUID generateV7() {
        long timestamp = System.currentTimeMillis();
        SecureRandom random = new SecureRandom();
        // Generate UUID v7 components
        long mostSigBits = (timestamp << 16);
        long leastSigBits = random.nextLong();
        return new UUID(mostSigBits, leastSigBits);
    }
}