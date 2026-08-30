package com.hdp.common.infrastructure.redis;

public record LettucePoolProperties(
        int shutdownTimeoutMs,
        Pool pool
) {
    public LettucePoolProperties {
        if (shutdownTimeoutMs == 0) shutdownTimeoutMs = 100;
    }

    public record Pool(
            int maxActive,
            int maxIdle,
            int minIdle,
            int maxWait
    ) {
        public Pool {
            if (maxActive == 0) maxActive = 8;
            if (maxIdle == 0) maxIdle = 8;
            if (minIdle == 0) minIdle = 0;
            if (maxWait == 0) maxWait = -1;
        }
    }
}
