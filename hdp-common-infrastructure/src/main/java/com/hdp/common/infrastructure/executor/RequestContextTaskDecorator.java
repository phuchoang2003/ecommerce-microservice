package com.hdp.common.infrastructure.executor;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

public class RequestContextTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // STEP 1: Capture parent thread's MDC context
        Map<String, String> parentMdcContext = MDC.getCopyOfContextMap();
        return () -> {
            // STEP 2: Preserve any existing MDC in child thread (for safety)
            Map<String, String> originalChildMdcContext = MDC.getCopyOfContextMap();

            try {
                // STEP 3: Restore parent MDC in child thread
                if (parentMdcContext != null) {
                    MDC.setContextMap(parentMdcContext);
                } else {
                    MDC.clear();
                }

                // STEP 4: Execute the original task
                runnable.run();

            } finally {
                // STEP 5: Restore original child MDC (prevent leaks)
                if (originalChildMdcContext != null) {
                    MDC.setContextMap(originalChildMdcContext);
                } else {
                    MDC.clear();
                }
            }
        };
    }
}
