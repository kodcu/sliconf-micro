package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.controller.RootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by ttayfur on 7/4/17.
 */

@SpringBootApplication
@EnableSwagger2
public class SliconfMicroApp {

    @Autowired
    public static RootController rootController;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SliconfMicroApp.class);
//        SpringApplication.run(SliconfMicroApp.class, args);

        application.run(args);

        //Enable CORS
        //CorsFilter.apply();
    }
}