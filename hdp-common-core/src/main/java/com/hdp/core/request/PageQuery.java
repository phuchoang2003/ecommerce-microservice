package com.hdp.core.request;

import java.util.List;

public record PageQuery(
    Integer page,
    Integer size,
    List<SortItem> sorts,
    List<FilterCriteria> filters
) {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;

    public PageQuery {
        if (page == null || page < 0) {
            page = DEFAULT_PAGE;
        }
        if (size == null || size <= 0) {
            size = DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            size = MAX_SIZE;
        }
    }

    public static PageQuery of(Integer page, Integer size) {
        return new PageQuery(page, size, null, null);
    }

    public static PageQuery of(Integer page, Integer size,
                               List<SortItem> sorts,
                               List<FilterCriteria> filters) {
        return new PageQuery(page, size, sorts, filters);
    }

    public static PageQuery of(Integer page, Integer size,
                               List<SortItem> sorts) {
        return new PageQuery(page, size, sorts, null);
    }
}