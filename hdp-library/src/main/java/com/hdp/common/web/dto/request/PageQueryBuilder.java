package com.hdp.common.web.dto.request;


import com.hdp.common.web.annotations.AllowedOperators;
import com.hdp.common.web.annotations.Filterable;
import com.hdp.common.web.annotations.Sortable;
import com.hdp.core.request.FilterCriteria;
import com.hdp.core.request.FilterOperator;
import com.hdp.core.request.PageQuery;
import com.hdp.core.request.SortItem;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Validates {@link PageQuery} against Projection annotations.
 *
 * <p>Only validates - does NOT build JPA Spec or Pageable.
 * Use persistence layer builders for that.</p>
 *
 * <h2>Architecture Flow</h2>
 * <pre>{@code
 * Controller (PageQuery) -> Usecase -> PageQueryBuilder.validate()
 *                                    ↓
 *                          FilterCriteriaBuilder, SortBuilder (persistence)
 *                                    ↓
 *                          Repository.findAll(specification, pageable)
 * }</pre>
 *
 * <h2>Usage</h2>
 * <pre>{@code
 * // 1. Validate query against projection
 * PageQueryBuilder.validate(query, OrderSummary.class);
 *
 * // 2. Build spec/pageable in persistence layer
 * Pageable pageable = PageRequestBuilder.build(query);
 * Specification<Order> spec = SpecificationQueryBuilder.build(query);
 * }</pre>
 */
public final class PageQueryBuilder {

    private static final Set<FilterOperator> DEFAULT_ALLOWED_OPERATORS = Set.of(
            FilterOperator.EQ, FilterOperator.NEQ, FilterOperator.LIKE,
            FilterOperator.GT, FilterOperator.GTE, FilterOperator.LT,
            FilterOperator.LTE, FilterOperator.IN, FilterOperator.BETWEEN,
            FilterOperator.IS_NULL, FilterOperator.IS_NOT_NULL);

    private PageQueryBuilder() {}

    /**
     * Validates PageQuery against projection annotations.
     * Throws {@link IllegalArgumentException} if validation fails.
     *
     * @param query           the page query from controller
     * @param projectionClass the projection interface with annotations
     */
    public static void validate(PageQuery query, Class<?> projectionClass) {
        validateSortFields(query.sorts(), projectionClass);
        validateFilterFields(query.filters(), projectionClass);
    }

    private static void validateSortFields(List<SortItem> sorts, Class<?> projectionClass) {
        if (sorts == null || sorts.isEmpty()) {
            return;
        }
        Set<String> allowedFields = getAllowedSortFields(projectionClass);
        for (SortItem sort : sorts) {
            if (!allowedFields.contains(sort.field())) {
                throw new IllegalArgumentException(
                        "Sort field '" + sort.field() + "' is not allowed for " + projectionClass.getSimpleName());
            }
        }
    }

    private static void validateFilterFields(List<FilterCriteria> filters, Class<?> projectionClass) {
        if (filters == null || filters.isEmpty()) {
            return;
        }
        Set<String> allowedFields = getAllowedFilterFields(projectionClass);
        Map<String, Set<FilterOperator>> fieldOperatorsMap = getFieldAllowedOperators(projectionClass);

        for (FilterCriteria filter : filters) {
            if (!allowedFields.contains(filter.field())) {
                throw new IllegalArgumentException(
                        "Filter field '" + filter.field() + "' is not allowed for " + projectionClass.getSimpleName());
            }
            Set<FilterOperator> allowedOps = fieldOperatorsMap.getOrDefault(
                    filter.field(), DEFAULT_ALLOWED_OPERATORS);
            if (!allowedOps.contains(filter.operator())) {
                throw new IllegalArgumentException(
                        "Operator " + filter.operator() + " is not allowed for field '" +
                                filter.field() + "' in " + projectionClass.getSimpleName());
            }
        }
    }

    private static Set<String> getAllowedSortFields(Class<?> projectionClass) {
        Sortable sortable = projectionClass.getAnnotation(Sortable.class);
        if (sortable == null) {
            return Set.of();
        }
        return new HashSet<>(Arrays.asList(sortable.fields()));
    }

    private static Set<String> getAllowedFilterFields(Class<?> projectionClass) {
        Filterable filterable = projectionClass.getAnnotation(Filterable.class);
        if (filterable == null) {
            return Set.of();
        }
        return new HashSet<>(Arrays.asList(filterable.fields()));
    }

    private static Map<String, Set<FilterOperator>> getFieldAllowedOperators(Class<?> projectionClass) {
        Map<String, Set<FilterOperator>> map = new HashMap<>();
        for (Field field : projectionClass.getDeclaredFields()) {
            AllowedOperators annotation = field.getAnnotation(AllowedOperators.class);
            if (annotation != null) {
                map.put(field.getName(), new HashSet<>(Arrays.asList(annotation.value())));
            }
        }
        return map;
    }
}