package com.hdp.common.web.utils;


import com.hdp.core.constant.RequestContextConstants;
import org.springframework.web.context.request.RequestAttributes;

public final class RequestContextUtils {
    private RequestContextUtils() {}

    public static String getTraceId() {
        var attrs = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();

        if (attrs == null) return null;

        var value = attrs.getAttribute(
                RequestContextConstants.TRACE_ID,
                RequestAttributes.SCOPE_REQUEST);
        return (value instanceof String s) ? s : null;
    }
}