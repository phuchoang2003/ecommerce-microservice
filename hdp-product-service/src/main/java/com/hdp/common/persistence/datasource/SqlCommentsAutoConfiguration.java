package com.hdp.common.persistence.datasource;

import com.hdp.common.infrastructure.info.ApplicationInfo;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

// datasource-proxy-spring-boot-starter autowires any QueryTransformer bean into the
// DataSource proxy it builds, so exposing the bean is all the wiring this needs.
@AutoConfiguration
public class SqlCommentsAutoConfiguration {

    @Bean
    public SqlCommentsQueryTransformer sqlCommentsQueryTransformer(ApplicationInfo applicationInfo) {
        return new SqlCommentsQueryTransformer(applicationInfo);
    }
}
