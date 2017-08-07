package javaday.istanbul.sliconf.micro.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.MXBean;
import java.util.List;
import java.util.Objects;


/**
 * Created by ttayfur on 7/8/17.
 */
public class CouchBaseConfig {
    private final Logger logger = LogManager.getLogger(getClass());

    private final static String HOST_NAME = "couchbase://127.0.0.1";
    private final static String BUCKET_PASS = "jfFMd8Nd";

    private final static String USERS_BUCKET_NAME = "users";
    private final static String EVENTS_BUCKET_NAME = "events";

    private static Cluster cluster;
    private static Bucket usersBucket;
    private static Bucket eventsBucket;


    public CouchBaseConfig() {
        if (Objects.isNull(cluster)) {
            cluster = CouchbaseCluster.create(HOST_NAME);
        }

        try {
            if (Objects.nonNull(cluster)) {
                usersBucket = openUsersBucket();
                eventsBucket = openEventsBucket();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Bucket openBucket(String bucketName, Bucket bucket) {
        try {
            if (Objects.nonNull(cluster)) {
                bucket = cluster.openBucket(bucketName, BUCKET_PASS);
            } else {
                bucket = null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return bucket;
    }

    private Bucket openUsersBucket() {
        return openBucket(USERS_BUCKET_NAME, usersBucket);
    }

    private Bucket openEventsBucket() {
        return openBucket(EVENTS_BUCKET_NAME, eventsBucket);
    }

    public Bucket getUsersBucket() {
        if (Objects.nonNull(usersBucket)) {
            return usersBucket;
        } else {
            return openUsersBucket();
        }
    }

    public void setUsersBucket(Bucket usersBucket) {
        CouchBaseConfig.usersBucket = usersBucket;
    }

    public Bucket getEventsBucket() {
        if (Objects.nonNull(eventsBucket)) {
            return eventsBucket;
        } else {
            return openEventsBucket();
        }
    }

    public void setEventsBucket(Bucket eventsBucket) { CouchBaseConfig.eventsBucket = eventsBucket; }

    private void createNewCluster() {

    }

    private void createNewBucket() {

    }

}
