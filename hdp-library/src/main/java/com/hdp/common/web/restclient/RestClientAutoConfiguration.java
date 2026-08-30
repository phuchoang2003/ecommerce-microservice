package com.hdp.common.web.restclient;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@AutoConfiguration
public class RestClientAutoConfiguration {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder()
                                .executor(Executors.newVirtualThreadPerTaskExecutor())
                                .build()))
                .build();
    }
}


