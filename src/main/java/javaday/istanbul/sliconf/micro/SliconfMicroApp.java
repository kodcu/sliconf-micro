package javaday.istanbul.sliconf.micro;


import io.swagger.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Created by ttayfur on 7/4/17.
 */
@EnableScheduling
@EnableMongoRepositories
//@SpringBootApplication
@EnableAutoConfiguration(
        exclude = {MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class,
                MongoRepositoriesAutoConfiguration.class})

@EnableConfigurationProperties
@ComponentScan
@Configuration
@SwaggerDefinition(
        host = "localhost:8090", //
        info = @Info(description = "Sliconf Micro API", //
                version = "V1.0.0", //
                title = "Sliconf Micro API for Web and Mobile", //
                contact = @Contact(name = "Taifuru", url = "http://sliconf.com")), //
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}, //
        consumes = {"application/json"}, //
        produces = {"application/json"}, //
        tags = {
                @Tag(name = "survey", description = "Survey Operations"),
                @Tag(name = "statistics", description = "Statistics about app"),
                @Tag(name = "user", description = "User related endpoints"),
                @Tag(name = "admin", description = "Admin related endpoints"),

        },

        securityDefinition = @SecurityDefinition(
                apiKeyAuthDefinitions = @ApiKeyAuthDefinition
                        (name = "Authorization", in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER, key = "Bearer"))
)

public class SliconfMicroApp {

    // Mongo documentlerini validate edebilmek icin bu iki bean gerekli.
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener(LocalValidatorFactoryBean lfb) {
        return new ValidatingMongoEventListener(lfb);
    }

    public static final String APP_PACKAGE = "javaday.istanbul.sliconf.micro";

    public static void main(String[] args) {
        SpringApplication.run(SliconfMicroApp.class, args);
    }
}