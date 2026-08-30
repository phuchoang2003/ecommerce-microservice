package com.hdp.common.persistence.datasource;

import com.hdp.common.infrastructure.info.ApplicationInfo;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
public class SqlCommentsAutoConfiguration {

    @Bean
    public SqlCommentsQueryTransformer sqlCommentsQueryTransformer(ApplicationInfo applicationInfo) {
        return new SqlCommentsQueryTransformer(applicationInfo);
    }
}
