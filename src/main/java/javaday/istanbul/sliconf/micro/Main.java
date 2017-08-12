package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CouchBaseConfig;
import javaday.istanbul.sliconf.micro.controller.RootController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ttayfur on 7/4/17.
 */

@ComponentScan("javaday.istanbul.sliconf.micro")
public class Main {
    // public static CouchBaseConfig couchBaseConfig;


    @Autowired
    public RootController rootController;

    public static void main(String[] args) {
        // couchBaseConfig = new CouchBaseConfig();

        // rootController = new RootController();

        ApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);
    }
}