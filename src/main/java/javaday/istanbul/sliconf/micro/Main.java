package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CouchBaseConfig;
import javaday.istanbul.sliconf.micro.controller.RootController;

/**
 * Created by ttayfur on 7/4/17.
 */
public class Main {
    public static CouchBaseConfig couchBaseConfig;

    public static void main(String[] args) {
        couchBaseConfig = new CouchBaseConfig();

        new RootController();
    }
}