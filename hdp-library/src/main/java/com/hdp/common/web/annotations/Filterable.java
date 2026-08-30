package com.hdp.common.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies which fields are allowed for filtering in a Projection.
 *
 * <p>Applied at the Projection interface level:</p>
 * <pre>{@code
 * @Filterable(fields = {"orderId", "status", "createdAt"})
 * public interface OrderSummary {
 *     String getOrderId();
 *     String getStatus();
 *     Instant getCreatedAt();
 *     // other getters not listed in @Filterable cannot be used for filtering
 * }
 * }</pre>
 *
 * <p>Only fields listed in @Filterable can be used in PageQuery.filters.</p>
 * This prevents filter injection attacks.
 *
 * <p>Use @AllowedOperators on individual fields to restrict which operators are allowed per field.</p>
 *
 * @see AllowedOperators
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Filterable {
    String[] fields();
}