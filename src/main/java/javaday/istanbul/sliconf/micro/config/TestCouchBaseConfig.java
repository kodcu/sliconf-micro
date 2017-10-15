package javaday.istanbul.sliconf.micro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.map.repository.config.MapRepositoryConfigurationExtension;


/**
 * Created by ttayfur on 7/8/17.
 */
@Configuration
@EnableMapRepositories(basePackages = {"javaday.istanbul.sliconf.micro.repository"})
@Profile("test")
public class TestCouchBaseConfig extends MapRepositoryConfigurationExtension {


}
