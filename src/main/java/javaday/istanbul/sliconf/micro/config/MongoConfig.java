package javaday.istanbul.sliconf.micro.config;

import com.mongodb.*;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ttayfur on 7/31/17.
 */
public class MongoConfig {

    private final static Logger logger = LogManager.getLogger(MongoConfig.class);

    public static MongoClient getMongoClient() {
        MongoClient client = null;
        try {
            ServerAddress address = new ServerAddress(Constants.MONGO_HOST, Constants.MONGO_PORT);
            MongoClientOptions options = MongoClientOptions.builder()
                    .serverSelectionTimeout(10 * 1000)
                    .connectTimeout(10 * 1000)
                    .socketTimeout(60 * 1000)
                    .connectionsPerHost(40)
                    .maxConnectionIdleTime(60 * 1000 * 5)
                    .readPreference(ReadPreference.primaryPreferred())
                    .writeConcern(WriteConcern.JOURNALED)
                    .build();

            final MongoCredential credential = MongoCredential.createCredential(Constants.AUTHENTICATED_USER_NAME, Constants.PROTECTED_DATABASE, Constants.PASSWORD_OF_AUTHENTICATED_USER_NAME.toCharArray());
            client = new MongoClient(Arrays.asList(address), Arrays.asList(credential), options);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
        return client;
    }
}
