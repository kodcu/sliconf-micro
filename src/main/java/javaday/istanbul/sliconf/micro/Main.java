package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CouchBaseConfig;
import javaday.istanbul.sliconf.micro.controller.TestController;
import javaday.istanbul.sliconf.micro.service.UserService;

/**
 * Created by ttayfur on 7/4/17.
 */
public class Main {
    public static void main(String[] args) {

        CouchBaseConfig couchBaseConfig = new CouchBaseConfig();

        couchBaseConfig.createCouchBase();

        // new TestController(new UserService());
    }
}