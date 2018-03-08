package javaday.istanbul.sliconf.micro.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


/**
 * Created by ttayfur on 7/3/18.
 */
@Configuration
@EnableMongoRepositories(basePackages = {"javaday.istanbul.sliconf.micro.repository"})
@Profile({"prod", "dev"})
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${sliconf.mongo.host-name}")
    private String hostName;

    @Value("${sliconf.mongo.port}")
    private int port;

    @Value("${sliconf.mongo.password}")
    private String password;

    @Value("${sliconf.mongo.username}")
    private String username;

    @Value("${sliconf.mongo.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public Mongo mongo() throws Exception {
        MongoCredential mongoCredential = MongoCredential
                .createCredential(username, "admin", password.toCharArray());

        ServerAddress serverAddress = new ServerAddress(hostName, port);

        return new MongoClient(serverAddress, Collections.singletonList(mongoCredential));
    }


    @Override
    protected Collection<String> getMappingBasePackages() {
        return Collections.singleton("javaday.istanbul.sliconf.micro");
    }


    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(
                LongToLocalDateTimeConverter.INSTANCE,
                LocalDateTimeToLongConverter.INSTANCE));
    }

    @ReadingConverter
    public enum LongToLocalDateTimeConverter implements Converter<Long, LocalDateTime> {
        INSTANCE;

        @Override
        public LocalDateTime convert(Long source) {
            return Instant.ofEpochMilli(source).atZone(ZoneId.of("Asia/Istanbul")).toLocalDateTime();
        }
    }

    @WritingConverter
    public enum LocalDateTimeToLongConverter implements Converter<LocalDateTime, Long> {
        INSTANCE;

        @Override
        public Long convert(LocalDateTime time) {
            return time.atZone(ZoneId.of("Asia/Istanbul")).toInstant().toEpochMilli();
        }
    }

}
