package com.hdp.core.core.test.extension;

import com.hdp.core.core.test.annotation.ExpectMaxQueryCount;
import com.hdp.core.core.test.annotation.QueryType;
import com.hdp.core.core.test.holder.QueryCountHolder;
import com.hdp.core.core.test.listener.QueryCountListener;
import org.junit.jupiter.api.extension.*;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JUnit 5 Extension that validates query count expectations.
 * Works with or without Spring context.
 */
public class QueryCountExtension implements BeforeEachCallback, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(QueryCountExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Object testInstance = context.getRequiredTestInstance();
        Method testMethod = context.getRequiredTestMethod();

        ExpectMaxQueryCount annotation = testMethod.getAnnotation(ExpectMaxQueryCount.class);
        if (annotation == null) {
            return;
        }

        DataSource dataSource = findDataSource(testInstance);
        if (dataSource == null) {
            return;
        }

        Set<QueryType> filterTypes = Arrays.stream(annotation.types())
                .collect(Collectors.toSet());

        QueryCountListener listener = QueryCountListener.createAndRegister(filterTypes, dataSource);

        // Store listener in ScopedValue for virtual thread safety
        QueryCountHolder.runWith(listener, () -> {
            // No-op: just set the listener in scope for the test
        });
    }

    @Override
    public void afterEach(ExtensionContext context) {
        QueryCountListener listener = QueryCountHolder.get();
        if (listener == null) {
            return;
        }

        Method testMethod = context.getRequiredTestMethod();
        ExpectMaxQueryCount annotation = testMethod.getAnnotation(ExpectMaxQueryCount.class);

        if (annotation == null) {
            return;
        }

        int maxQueries = annotation.value();
        int actualQueries = getActualQueryCount(listener, annotation.types());

        if (actualQueries > maxQueries) {
            String message = String.format(
                    "Query count exceeded expectation.%n" +
                    "  Expected: <= %d%n" +
                    "  Actual: %d%n" +
                    "  SELECT: %d, INSERT: %d, UPDATE: %d, DELETE: %d%n" +
                    "  Test: %s",
                    maxQueries, actualQueries,
                    listener.getSelectCount(), listener.getInsertCount(),
                    listener.getUpdateCount(), listener.getDeleteCount(),
                    testMethod.getName()
            );
            throw new AssertionError(message);
        }

        listener.reset();
    }

    private int getActualQueryCount(QueryCountListener listener, QueryType[] types) {
        if (types == null || types.length == 0) {
            return listener.getTotalCount();
        }
        return Arrays.stream(types)
                .mapToInt(listener::getCountForType)
                .sum();
    }

    private DataSource findDataSource(Object testInstance) {
        // Try Spring Boot Test if available
        DataSource ds = findSpringDataSource(testInstance);
        if (ds != null) {
            return ds;
        }

        // Fallback: try to find DataSource field directly
        return findDataSourceField(testInstance);
    }

    private DataSource findSpringDataSource(Object testInstance) {
        try {
            // Check for Spring Boot Test annotation
            Class<?> clazz = testInstance.getClass();
            while (clazz != null) {
                for (var annotation : clazz.getAnnotations()) {
                    if (annotation.annotationType().getName().contains("SpringBootTest")) {
                        // Try to get ApplicationContext via static context holder
                        return getDataSourceFromSpringContext();
                    }
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            // Fallback to field lookup
        }
        return null;
    }

    private DataSource getDataSourceFromSpringContext() {
        try {
            // Use Spring Boot's test context utilities
            Class<?> testContextAnnotation = Class.forName("org.springframework.test.context.junit.jupiter.SpringExtension");
            var field = testContextAnnotation.getDeclaredField("contextManager");
            field.setAccessible(true);
            Object contextManager = field.get(testContextAnnotation);

            if (contextManager != null) {
                var getMethod = contextManager.getClass().getMethod("getApplicationContext");
                Object appContext = getMethod.invoke(contextManager);
                if (appContext != null) {
                    var getBeanMethod = appContext.getClass().getMethod("getBean", Class.class);
                    return (DataSource) getBeanMethod.invoke(appContext, DataSource.class);
                }
            }
        } catch (Exception e) {
            // Not in Spring context
        }
        return null;
    }

    private DataSource findDataSourceField(Object testInstance) {
        Class<?> clazz = testInstance.getClass();
        while (clazz != null) {
            for (var field : clazz.getDeclaredFields()) {
                if (field.getType() == DataSource.class) {
                    try {
                        field.setAccessible(true);
                        return (DataSource) field.get(testInstance);
                    } catch (Exception e) {
                        // Skip this field
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }
}
