package javaday.istanbul.sliconf.micro.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import spark.servlet.SparkFilter;

import javax.servlet.Filter;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class SpringConfigurations {

    @Bean
    public Properties eventProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("eventController.properties");
    }

    @Bean
    public Properties loginProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("loginController.properties");
    }

    @Bean
    public Properties userRepositoryProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("userRepository.properties");
    }

    @Bean
    public Properties mailMessageProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("mailMessages.properties");
    }

    @Bean
    public FilterRegistrationBean sparkFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sparkFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("applicationClass", "javaday.istanbul.sliconf.micro.SliconfMicroApp");
        registration.setName("SparkFilter");
        registration.setOrder(1);
        return registration;
    }

    private Filter sparkFilter() {
        return new SparkFilter();
    }
}