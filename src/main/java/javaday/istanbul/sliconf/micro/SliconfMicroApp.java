package javaday.istanbul.sliconf.micro;


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
public class SliconfMicroApp implements SparkApplication {

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