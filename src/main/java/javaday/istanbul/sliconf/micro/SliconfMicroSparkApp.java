package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CorsFilter;
import javaday.istanbul.sliconf.micro.controller.RootController;
import org.springframework.beans.factory.annotation.Autowired;
import spark.servlet.SparkApplication;


/**
 * Created by ttayfur on 10/20/17.
 */
public class SliconfMicroSparkApp implements SparkApplication {

    @Autowired
    public static RootController rootController;

    @Override
    public void init() {

        //Enable CORS
        CorsFilter.apply();
        RootController.setPaths();
    }
}