package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.controller.RootController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by ttayfur on 7/4/17.
 */

@ComponentScan("javaday.istanbul.sliconf.micro")
public class Main {

    @Autowired
    public RootController rootController;

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
    }
}