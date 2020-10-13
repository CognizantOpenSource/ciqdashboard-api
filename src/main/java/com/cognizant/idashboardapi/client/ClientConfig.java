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

    @Value("${app.npl.api.client.url}")
    private String appNPLApiClientURL;

    @Bean
    public NLPApiClient getAuthApiClient(){
        return Feign.builder()
                .contract(new SpringMvcContract())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Slf4jLogger(NLPApiClient.class))
                .logLevel(Logger.Level.FULL)
                .target(NLPApiClient.class, appNPLApiClientURL);
    }

}
