package javaday.istanbul.sliconf.micro;


import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import javaday.istanbul.sliconf.micro.config.CorsFilter;
import javaday.istanbul.sliconf.micro.controller.RootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import spark.servlet.SparkApplication;

/**
 * Created by ttayfur on 7/4/17.
 */

@SpringBootApplication
@SwaggerDefinition(host = "localhost:8090", //
        info = @Info(description = "Sliconf Micro API", //
                version = "V0.0.1", //
                title = "Sliconf Micro API for Web and Mobile", //
                contact = @Contact(name = "Taifuru", url = "http://sliconf.com")), //
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS}, //
        consumes = {"application/json"}, //
        produces = {"application/json"}, //
        tags = {@Tag(name = "swagger")})
public class SliconfMicroApp implements SparkApplication {

    public static final String APP_PACKAGE = "javaday.istanbul.sliconf.micro";

    @Autowired
    public static RootController rootController;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SliconfMicroApp.class);
        application.run(args);
    }

    @Override
    public void init() {

        //Enable CORS
        CorsFilter.apply();
        RootController.setPaths();
    }
}