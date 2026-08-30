package com.hdp.common.infrastructure.info;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(ApplicationInfo.class)
public class ApplicationInfoAutoConfiguration {
}
