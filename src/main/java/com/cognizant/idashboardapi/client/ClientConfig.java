package com.cognizant.idashboardapi.client;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    @Value("${app.nlp.api.client.url}")
    private String appNLPApiClientURL;
    @Value("${app.auth.client.url}")
    private String appAuthClientURL;

    @Bean
    public AuthApiClient getAuthApiClient(){
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(AuthApiClient.class))
                .logLevel(Logger.Level.FULL)
                .target(AuthApiClient.class, appAuthClientURL);
    }

    @Bean
    public NLPApiClient getNLPApiClient(){
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(NLPApiClient.class))
                .logLevel(Logger.Level.FULL)
                .target(NLPApiClient.class, appNLPApiClientURL);
    }

}
