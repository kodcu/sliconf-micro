package javaday.istanbul.sliconf.micro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.Arrays;
import java.util.List;


/**
 * Created by ttayfur on 7/8/17.
 */
@Configuration
@EnableCouchbaseRepositories(basePackages = {"javaday.istanbul.sliconf.micro.repository"})
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

    private final static String HOST_NAME = "127.0.0.1";
    private final static String BUCKET_PASS = "jfFMd8Nd";

    private final static String USERS_BUCKET_NAME = "users";
    private final static String EVENTS_BUCKET_NAME = "events";

    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList("localhost", HOST_NAME);
    }

    @Override
    protected String getBucketName() {
        return USERS_BUCKET_NAME;
    }

    @Override
    protected String getBucketPassword() {
        return BUCKET_PASS;
    }

    @Override
    protected String getMappingBasePackage() {
        return "javaday.istanbul.sliconf.micro.model";
    }


}
