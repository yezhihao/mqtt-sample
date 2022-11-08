package org.sample.mqtt.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Created by Alan on 2017/2/22.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("mqtt-server")
                .description("mqtt api")
                .license("")
                .licenseUrl("")
                .termsOfServiceUrl("https://gitee.com/yezhihao")
                .version("1.0.0")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket customImplementation(TypeResolver resolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.sample.mqtt.controller"))
                .build()
                .apiInfo(apiInfo())
                .alternateTypeRules(
                        AlternateTypeRules.newRule(resolver.resolve(Mono.class, WildcardType.class), resolver.resolve(WildcardType.class)),
                        AlternateTypeRules.newRule(resolver.resolve(Flux.class, WildcardType.class), resolver.resolve(List.class, WildcardType.class))
                );
    }
}