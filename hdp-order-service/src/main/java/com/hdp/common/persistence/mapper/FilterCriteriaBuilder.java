package com.hdp.common.persistence.mapper;

import com.hdp.common.persistence.utils.SqlUtils;
import com.hdp.core.request.FilterCriteria;
import com.hdp.core.request.FilterOperator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds JPA {@link jakarta.persistence.criteria.Predicate} from filter criteria.
 *
 * <p>This class is persistence-agnostic with SqlUtils dependency.</p>
 */
public final class FilterCriteriaBuilder {
    private FilterCriteriaBuilder() {}

    public static <T> Predicate[] buildPredicates(
            Root<T> root,
            CriteriaQuery<?> cq,
            CriteriaBuilder cb,
            List<FilterCriteria> filters) {

        if (filters == null || filters.isEmpty()) {
            return new Predicate[0];
        }

        List<Predicate> predicates = new ArrayList<>();

        for (FilterCriteria filter : filters) {
            Path<?> fieldPath = getFieldPath(root, filter.field());
            if (fieldPath == null) {
                continue;
            }
            predicates.add(buildPredicate(cb, fieldPath, filter.operator(), filter.value()));
        }

        return predicates.toArray(new Predicate[0]);
    }

    private static Path<?> getFieldPath(Root<?> root, String field) {
        if (field.contains(".")) {
            String[] parts = field.split("\\.", 2);
            Join<?, ?> join = root.join(parts[0], JoinType.LEFT);
            return getNestedPath(join, parts[1]);
        }
        try {
            return root.get(field);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Path<?> getNestedPath(Join<?, ?> join, String remainingField) {
        if (remainingField.contains(".")) {
            String[] parts = remainingField.split("\\.", 2);
            Join<?, ?> nextJoin = join.join(parts[0], JoinType.LEFT);
            return getNestedPath(nextJoin, parts[1]);
        }
        return join.get(remainingField);
    }

    private static Predicate buildPredicate(CriteriaBuilder cb, Path<?> path,
                                           FilterOperator operator, Object value) {
        return switch (operator) {
            case EQ -> cb.equal(path, value);
            case NEQ -> cb.notEqual(path, value);
            case GT -> cb.greaterThan((Path<Comparable>) path, (Comparable) value);
            case GTE -> cb.greaterThanOrEqualTo((Path<Comparable>) path, (Comparable) value);
            case LT -> cb.lessThan((Path<Comparable>) path, (Comparable) value);
            case LTE -> cb.lessThanOrEqualTo((Path<Comparable>) path, (Comparable) value);
            case LIKE -> cb.like(cb.lower((Path<String>) path),
                    SqlUtils.likeEscape(value.toString().toLowerCase()));
            case IN -> path.in(value);
            case BETWEEN -> {
                if (value instanceof List<?> list && list.size() == 2) {
                    yield cb.between((Path<Comparable>) path,
                            (Comparable) list.get(0), (Comparable) list.get(1));
                }
                yield cb.conjunction();
            }
            case IS_NULL -> cb.isNull(path);
            case IS_NOT_NULL -> cb.isNotNull(path);
        };
    }
}