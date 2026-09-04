package com.hdp.common.web.i18n;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;


@AutoConfiguration
public class MessageSourceAutoConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
            "classpath:messages-validation",
            "classpath:messages-business",
            "classpath:messages-order"
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(300);
        messageSource.setDefaultLocale(java.util.Locale.ENGLISH);
        return messageSource;
    }
}