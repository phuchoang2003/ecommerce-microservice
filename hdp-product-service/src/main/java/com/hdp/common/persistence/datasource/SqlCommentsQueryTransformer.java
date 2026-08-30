package com.hdp.common.persistence.datasource;

import com.hdp.common.infrastructure.info.ApplicationInfo;
import com.hdp.common.web.utils.RequestContextUtils;
import com.hdp.core.constant.RequestContextConstants;
import net.ttddyy.dsproxy.transform.QueryTransformer;
import net.ttddyy.dsproxy.transform.TransformInfo;

public class SqlCommentsQueryTransformer implements QueryTransformer {

    private static final String HOST = "host";
    private static final String ENV = "env";

    private final ApplicationInfo applicationInfo;

    public SqlCommentsQueryTransformer(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    @Override
    public String transformQuery(TransformInfo transformInfo) {
        String comment = SqlCommentBuilder.builder()
                .add(RequestContextConstants.SERVICE, applicationInfo.serviceName())
                .add(HOST, applicationInfo.hostname())
                .add(ENV, applicationInfo.environment())
                .add(RequestContextConstants.TRACE_ID, RequestContextUtils.getTraceId())
                .build();

        return comment + transformInfo.getQuery();
    }
}
