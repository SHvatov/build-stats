package com.build.stats.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ResourceConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler(
            "/webjars/**",
            "/img/**",
            "/css/**",
            "/js/**"
        ).addResourceLocations(
            "classpath:/META-INF/resources/webjars/",
            "classpath:/static/img/",
            "classpath:/static/css/",
            "classpath:/static/js/"
        )
    }
}