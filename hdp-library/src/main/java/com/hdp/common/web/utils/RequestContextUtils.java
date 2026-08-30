package com.hdp.common.web.utils;


import com.hdp.core.constant.RequestContextConstants;
import org.slf4j.MDC;

public final class RequestContextUtils {
    private RequestContextUtils() {}

    public static String getTraceId() {
        return MDC.get(RequestContextConstants.TRACE_ID);
    }
}
