package com.hdp.common.persistence.datasource;

import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@AutoConfiguration
@ConditionalOnBean(DataSource.class)
public class DataSourceProxyAutoConfiguration {

    @Bean
    public DataSourceProxyBeanPostProcessor dataSourceProxyBeanPostProcessor() {
        return new DataSourceProxyBeanPostProcessor();
    }

    public class DataSourceProxyBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            if (bean instanceof DataSource ds) {

                SLF4JQueryLoggingListener loggingListener = new SLF4JQueryLoggingListener();
                loggingListener.setQueryLogEntryCreator(new DefaultQueryLogEntryCreator());

                return ProxyDataSourceBuilder
                        .create(ds)
                        .name("DS-PROXY")
                        .listener(loggingListener)
                        .build();
            }
            return bean;
        }
    }
}
