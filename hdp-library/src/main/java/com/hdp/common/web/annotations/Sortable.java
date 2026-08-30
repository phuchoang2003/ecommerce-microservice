package com.hdp.common.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies which fields are allowed for sorting in a Projection.
 *
 * <p>Applied at the Projection interface level:</p>
 * <pre>{@code
 * @Sortable(fields = {"orderId", "status", "createdAt"})
 * public interface OrderSummary {
 *     String getOrderId();
 *     String getStatus();
 *     Instant getCreatedAt();
 *     // other getters not listed in @Sortable cannot be used for sorting
 * }
 * }</pre>
 *
 * <p>Only fields listed in @Sortable can be used in PageQuery.sortItems.</p>
 * This prevents sort injection attacks.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sortable {
    String[] fields();
}