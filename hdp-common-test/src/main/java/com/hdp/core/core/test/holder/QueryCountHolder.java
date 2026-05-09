package com.hdp.core.core.test.holder;

import com.hdp.core.core.test.listener.QueryCountListener;
import java.lang.ScopedValue;
import java.util.concurrent.Callable;

/**
 * Holder for QueryCountListener using ScopedValue for virtual thread compatibility.
 * Automatically cleaned up when scope ends - no manual cleanup needed.
 */
public final class QueryCountHolder {

    private QueryCountHolder() {
    }

    /**
     * ScopedValue to hold the current QueryCountListener.
     * Works correctly with virtual threads.
     */
    public static final ScopedValue<QueryCountListener> CURRENT = ScopedValue.newInstance();

    /**
     * Run a callable with a listener attached to the current scope.
     */
    public static <T> T runWith(QueryCountListener listener, Callable<T> callable) throws Exception {
        return ScopedValue.where(CURRENT, listener)
                .call(callable::call);
    }

    /**
     * Run a runnable with a listener attached to the current scope.
     */
    public static void runWith(QueryCountListener listener, Runnable runnable) {
        ScopedValue.where(CURRENT, listener)
                .run(runnable);
    }

    /**
     * Get the current listener from scope.
     */
    public static QueryCountListener get() {
        return CURRENT.get();
    }
}
