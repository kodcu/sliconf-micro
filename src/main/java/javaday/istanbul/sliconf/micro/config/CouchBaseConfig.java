package javaday.istanbul.sliconf.micro.config;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JavaType;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.type.SimpleType;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.type.TypeFactory;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.util.Converter;
import com.couchbase.client.java.Bucket;
import javaday.istanbul.sliconf.micro.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.CustomConversions;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

import java.util.Arrays;
import java.util.Base64;
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
    private final static String DEFAULT_BUCKET_NAME = "default";

    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList("localhost", HOST_NAME);
    }

    @Override
    protected String getBucketName() {
        return DEFAULT_BUCKET_NAME;
    }

    @Override
    protected String getBucketPassword() {
        return "";
    }


    @Override
    protected String getMappingBasePackage() {
        return "javaday.istanbul.sliconf.micro";
    }


    @Bean
    public Bucket usersBucket() throws Exception {
        return couchbaseCluster().openBucket(USERS_BUCKET_NAME, BUCKET_PASS);
    }

    @Bean(name = "usersTemplate")
    public CouchbaseTemplate usersTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), usersBucket(),
                mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            baseMapping.mapEntity(User.class, usersTemplate());
        } catch (Exception e) {
            LoggerFactory.getLogger("asd").error(e.getMessage(), e);
            // Todo custom Exception handling
        }
    }

}
