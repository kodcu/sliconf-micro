package javaday.istanbul.sliconf.micro;


import javaday.istanbul.sliconf.micro.config.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import spark.servlet.SparkApplication;


/**
 * Created by ttayfur on 10/20/17.
 * <p>
 * Bu sinif SparkFiltresinde kullanilmaktadir
 */
public class SliconfMicroSparkApp implements SparkApplication {

    @Autowired
    public static RootController rootController; // NOSONAR

    @Override
    public void init() {

        //Enable CORS
        CorsFilter.apply();
        RootController.setPaths();
    }
}