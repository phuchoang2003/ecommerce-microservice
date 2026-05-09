package com.hdp.core.core.test.listener;

import com.hdp.core.core.test.annotation.QueryType;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;


import javax.sql.DataSource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Query execution listener that counts queries by type.
 * Works with datasource-proxy-spring-boot-starter library.
 */
public class QueryCountListener implements QueryExecutionListener {

    private final AtomicInteger totalCount = new AtomicInteger();
    private final AtomicInteger selectCount = new AtomicInteger();
    private final AtomicInteger insertCount = new AtomicInteger();
    private final AtomicInteger updateCount = new AtomicInteger();
    private final AtomicInteger deleteCount = new AtomicInteger();

    private final Set<QueryType> filterTypes;
    private ProxyDataSource proxyDataSource;

    public QueryCountListener(Set<QueryType> filterTypes) {
        this.filterTypes = filterTypes;
    }

    @Override
    public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
        // no-op
    }

    @Override
    public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
        for (QueryInfo queryInfo : queryInfoList) {
            QueryType queryType = mapOperationToQueryType(queryInfo.getQuery());

            if (filterTypes.isEmpty() || filterTypes.contains(queryType)) {
                totalCount.incrementAndGet();

                switch (queryType) {
                    case SELECT -> selectCount.incrementAndGet();
                    case INSERT -> insertCount.incrementAndGet();
                    case UPDATE -> updateCount.incrementAndGet();
                    case DELETE -> deleteCount.incrementAndGet();
                }
            }
        }
    }

    private QueryType mapOperationToQueryType(String operation) {
        return switch (operation.toUpperCase()) {
            case "SELECT" -> QueryType.SELECT;
            case "INSERT" -> QueryType.INSERT;
            case "UPDATE" -> QueryType.UPDATE;
            case "DELETE" -> QueryType.DELETE;
            default -> QueryType.SELECT;
        };
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public int getSelectCount() {
        return selectCount.get();
    }

    public int getInsertCount() {
        return insertCount.get();
    }

    public int getUpdateCount() {
        return updateCount.get();
    }

    public int getDeleteCount() {
        return deleteCount.get();
    }

    public int getCountForType(QueryType type) {
        return switch (type) {
            case SELECT -> selectCount.get();
            case INSERT -> insertCount.get();
            case UPDATE -> updateCount.get();
            case DELETE -> deleteCount.get();
        };
    }

    public void reset() {
        totalCount.set(0);
        selectCount.set(0);
        insertCount.set(0);
        updateCount.set(0);
        deleteCount.set(0);
    }

    /**
     * Create a QueryCountListener and register it to a ProxyDataSource wrapping the original DataSource.
     * The proxy is stored to prevent GC.
     */
    public static QueryCountListener createAndRegister(Set<QueryType> filterTypes, DataSource originalDataSource) {
        QueryCountListener listener = new QueryCountListener(filterTypes);

        ProxyDataSource proxyDs = ProxyDataSourceBuilder
                .create(originalDataSource)
                .name("QueryCountProxy")
                .listener(listener)
                .build();

        listener.proxyDataSource = proxyDs;

        return listener;
    }

    /**
     * Get the proxied DataSource that should be used in the test context.
     * Returns null if createAndRegister was not called.
     */
    public DataSource getProxyDataSource() {
        return proxyDataSource;
    }
}
