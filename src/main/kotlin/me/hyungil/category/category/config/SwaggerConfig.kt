package me.hyungil.category.category.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("me.hyungil.category.category.presentation"))
        .paths(PathSelectors.regex("/.*"))
        .build()
        .apiInfo(apiInfo())


    private fun apiInfo() = ApiInfoBuilder().apply {
        title("Category API - Jeong Hyung Il")
        description("API Endpoint for serving file")
    }.build()
}