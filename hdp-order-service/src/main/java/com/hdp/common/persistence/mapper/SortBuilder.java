package com.hdp.common.persistence.mapper;

import com.hdp.core.request.SortDirection;
import com.hdp.core.request.SortItem;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds Spring {@link Sort} from domain {@link SortItem} list.
 *
 * <p>Maps domain sort direction to Spring Sort.Direction.</p>
 */
public final class SortBuilder {

    private SortBuilder() {}

    public static Sort build(List<SortItem> sortItems) {
        if (sortItems == null || sortItems.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (SortItem item : sortItems) {
            orders.add(new Sort.Order(
                    mapDirection(item.direction()),
                    item.field()
            ));
        }

        return Sort.by(orders);
    }

    private static Sort.Direction mapDirection(
            SortDirection direction) {
        return switch (direction) {
            case ASC -> Sort.Direction.ASC;
            case DESC -> Sort.Direction.DESC;
        };
    }
}