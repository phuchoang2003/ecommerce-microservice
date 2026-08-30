package com.hdp.common.persistence.mapper;

import com.hdp.core.request.PageQuery;
import org.springframework.data.domain.Pageable;

/**
 * Builds Spring {@link Pageable} from domain {@link PageQuery}.
 *
 * <p>Combines {@link SortBuilder} with pagination info.</p>
 */
public final class PageRequestBuilder {

    private PageRequestBuilder() {}

    public static Pageable build(PageQuery query) {
        return org.springframework.data.domain.PageRequest.of(
                query.page(),
                query.size(),
                SortBuilder.build(query.sorts())
        );
    }
}