package com.volvo.cars.congestiontaxcalculator.swagger;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket postsApi() {
    return new Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Volvo Car - congestion-tax-calculator").description(
            "Volvo Car - congestion-tax-calculator is a API tax calculator service based on the Vehicle types")
        .termsOfServiceUrl("http://dummy-termsandconditions.com")
        .contact(new Contact("Raja Maragani", "https://github.com/raja-maragani",
            "rajamaragani@gmail.com"))
        .license("Volvo Car @2021").licenseUrl("support@dummy.com").version("1.0").build();
  }

}
