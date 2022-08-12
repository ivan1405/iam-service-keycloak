package com.chapter.backend.services.iamservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

@Configuration
@EnableSwagger2
@EnableJpaRepositories(basePackages = "com.chapter.backend.services.iamservice.crud.repository")
@EntityScan(basePackages = "com.chapter.backend.services.iamservice.crud.entity")
@ComponentScan(basePackages = {"com.chapter.backend.services"})
public class ServiceConfig {

    @Bean
    public Docket api() {
        final String BASE_PACKAGE = "com.chapter.backend.services.iamservice";
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("IAM Service API")
                        .description("IAM Service API description")
                        .contact(new Contact("Parser Digital", "https://parserdigital.com", "ivan.perez@parserdigital.com"))
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .apis(Predicate.not(RequestHandlerSelectors.basePackage(BASE_PACKAGE + ".swagger")))
                .build();
    }
}