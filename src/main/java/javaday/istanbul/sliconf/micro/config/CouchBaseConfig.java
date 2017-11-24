package javaday.istanbul.sliconf.micro.config;

import com.couchbase.client.java.Bucket;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Comment;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.token.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.convert.CustomConversions;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;


/**
 * Created by ttayfur on 7/8/17.
 */
@Configuration
@Profile({"prod", "dev"})
public class CouchBaseConfig extends AbstractCouchbaseConfiguration {

    private final Logger logger = LoggerFactory.getLogger(CouchBaseConfig.class);

    @Value("${sliconf.couchbase.default-host-name}")
    private String defaultHostName;

    @Value("${sliconf.couchbase.host-name}")
    private String hostName;

    @Value("${sliconf.couchbase.bucket-pass}")
    private String bucketPass;

    private static final String USERS_BUCKET_NAME = "users";
    private static final String EVENTS_BUCKET_NAME = "events";
    private static final String DEFAULT_BUCKET_NAME = "default";
    private static final String TOKENS_BUCKET_NAME = "tokens";
    private static final String COMMENTS_BUCKET_NAME = "comments";

    @Override
    protected List<String> getBootstrapHosts() {
        return Arrays.asList(defaultHostName, hostName);
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
        return couchbaseCluster().openBucket(USERS_BUCKET_NAME, bucketPass);
    }

    @Bean(name = "usersTemplate")
    public CouchbaseTemplate usersTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), usersBucket(),
                mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Bean
    public Bucket eventsBucket() throws Exception {
        return couchbaseCluster().openBucket(EVENTS_BUCKET_NAME, bucketPass);
    }

    @Bean(name = "eventsTemplate")
    public CouchbaseTemplate eventsTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), eventsBucket(),
                mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Bean
    public Bucket tokensBucket() throws Exception {
        return couchbaseCluster().openBucket(TOKENS_BUCKET_NAME, bucketPass);
    }

    @Bean(name = "tokensTemplate")
    public CouchbaseTemplate tokensTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), tokensBucket(),
                mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Bean
    public Bucket commentsBucket() throws Exception {
        return couchbaseCluster().openBucket(COMMENTS_BUCKET_NAME, bucketPass);
    }

    @Bean(name = "commentsTemplate")
    public CouchbaseTemplate commentsTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(
                couchbaseClusterInfo(), commentsBucket(),
                mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            baseMapping.mapEntity(Event.class, eventsTemplate());
            baseMapping.mapEntity(User.class, usersTemplate());
            baseMapping.mapEntity(Token.class, tokensTemplate());
            baseMapping.mapEntity(Comment.class, commentsTemplate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(StringToByteConverter.INSTANCE,
                ByteToStringConverter.INSTANCE));
    }

    @ReadingConverter
    public enum StringToByteConverter implements Converter<String, byte[]> {
        INSTANCE;

        @Override
        public byte[] convert(String source) {
            return Base64.getDecoder().decode(source);
        }
    }

    @WritingConverter
    public enum ByteToStringConverter implements Converter<byte[], String> {
        INSTANCE;

        @Override
        public String convert(byte[] bytes) {
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

}
