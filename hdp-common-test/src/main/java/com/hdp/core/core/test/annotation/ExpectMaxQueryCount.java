package com.hdp.core.core.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Expects the test to execute no more than the specified number of queries.
 * Can filter by query type (SELECT, INSERT, UPDATE, DELETE).
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectMaxQueryCount {

    /**
     * Maximum number of queries allowed.
     */
    int value();

    /**
     * Query types to count. If empty, counts all query types.
     */
    QueryType[] types() default {};
}
