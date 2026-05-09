package com.hdp.common.persistence.mapper;

import com.hdp.core.request.FilterCriteria;
import com.hdp.core.request.PageQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Builds JPA {@link Specification} from domain {@link PageQuery}.
 *
 * <p>Uses {@link FilterCriteriaBuilder} for predicate building.</p>
 */
public final class SpecificationQueryBuilder {

    private SpecificationQueryBuilder() {}

    public static <T> Specification<T> build(PageQuery query) {
        List<FilterCriteria> filters = query.filters();
        if (filters == null || filters.isEmpty()) {
            return null;
        }

        return (Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Predicate[] predicates = FilterCriteriaBuilder.buildPredicates(
                    root, cq, cb, filters);
            return cb.and(predicates);
        };
    }
}