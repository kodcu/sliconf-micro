package javaday.istanbul.sliconf.micro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

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
}