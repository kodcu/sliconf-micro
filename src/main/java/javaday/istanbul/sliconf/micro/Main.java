package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CorsFilter;
import javaday.istanbul.sliconf.micro.controller.RootController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ttayfur on 7/4/17.
 */

@ComponentScan("javaday.istanbul.sliconf.micro")
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    public static RootController rootController;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        //Enable CORS
        CorsFilter.apply();
        logger.info(context.getApplicationName() + ": is started!");
    }
}