package com.hdp.core.query;

public interface QueryHandler<Q, V> {
    V handle(Q query);
}
