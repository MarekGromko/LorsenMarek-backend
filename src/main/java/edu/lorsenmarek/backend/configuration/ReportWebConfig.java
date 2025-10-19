package edu.lorsenmarek.backend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Open endpoints to allow access to generated reports about the application
 */
@Configuration
public class ReportWebConfig implements WebMvcConfigurer {
    /** {@link ReportWebConfig} should not be instantiated */
    public ReportWebConfig() {}
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/public/apidocs/**")
                .addResourceLocations("file:target/reports/apidocs/");
        registry.addResourceHandler("/public/coverage/**")
                .addResourceLocations("file:target/site/jacoco/");
    }
}