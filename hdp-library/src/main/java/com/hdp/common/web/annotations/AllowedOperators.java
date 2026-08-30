package com.hdp.common.web.annotations;



import com.hdp.core.request.FilterOperator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Restricts which FilterOperators are allowed for a specific field.
 *
 * <p>Applied at field level within a Projection interface:</p>
 * <pre>{@code
 * @Filterable(fields = {"orderId", "status", "createdAt", "name"})
 * public interface OrderSummary {
 *     String getOrderId();  // no annotation -> all operators allowed
 *
 *     @AllowedOperators({FilterOperator.EQ, FilterOperator.NEQ, FilterOperator.IN})
 *     String getStatus();   // only EQ, NEQ, IN allowed
 *
 *     @AllowedOperators({FilterOperator.GTE, FilterOperator.LTE, FilterOperator.BETWEEN})
 *     Instant getCreatedAt();  // only range operators allowed
 *
 *     String getName();     // no annotation -> all operators allowed
 * }
 * }</pre>
 *
 * <p>If a field has no @AllowedOperators annotation, all operators are allowed by default.</p>
 *
 * @see Filterable
 * @see FilterOperator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedOperators {
    FilterOperator[] value();
}