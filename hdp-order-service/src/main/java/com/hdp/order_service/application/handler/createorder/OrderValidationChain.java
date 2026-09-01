package com.hdp.order_service.application.handler.createorder;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderValidationChain {
    @Bean
    public OrderValidationStep orderValidation(CustomerValidationStep step1,
                                               FraudValidationStep step2) {
        return step1
                .next(step2);

    }

}
