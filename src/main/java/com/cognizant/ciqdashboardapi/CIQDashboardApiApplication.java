package com.cognizant.ciqdashboardapi;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Arrays;

@SpringBootApplication
@EnableMongoAuditing
@EnableAutoConfiguration
@EnableEncryptableProperties
public class CIQDashboardApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CIQDashboardApiApplication.class, args);
    }

    @Configuration
    public class SpringDocOpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
            return new OpenAPI()
                    .components(new Components().addSecuritySchemes("bearer-jwt",
                            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                    .in(SecurityScheme.In.HEADER).name("Authorization")))
                    .info(getInfo())
                    .addSecurityItem(
                            new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
        }

        private Info getInfo() {
            return new Info()
                    .title("CIQDashBoard REST API")
                    .description("CIQDashBoard API REST calls using Spring boot")
                    .version("1.0")
                    .termsOfService("Terms of service")
                    .contact(new Contact().name("CIQDashboard").email("cognizantintegratedqualitydashboard@cognizant.com").url("www.Cognizant.com"))
                    .license(new License().name("License of API").url("API license URL"));
        }

    }

}