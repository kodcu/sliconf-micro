package javaday.istanbul.sliconf.micro.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


/**
 * Created by ttayfur on 7/8/17.
 */
public class CouchBaseConfig {
    private final Logger logger = LogManager.getLogger(getClass());

    private final static String HOST_NAME = "couchbase://127.0.0.1";
    private final static String USERS_BUCKET_NAME = "users";
    private final static String BUCKET_PASS = "jfFMd8Nd";

    private static Cluster cluster;
    private static Bucket usersBucket;

    public CouchBaseConfig() {
        if (Objects.isNull(cluster)) {
            cluster = CouchbaseCluster.create(HOST_NAME);
        }

        try {
            if (Objects.nonNull(cluster)) {
                usersBucket = openUsersBucket();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Bucket openUsersBucket() {
        try {
            if (Objects.nonNull(cluster)) {
                usersBucket = cluster.openBucket(USERS_BUCKET_NAME, BUCKET_PASS);
            } else {
                usersBucket = null;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return usersBucket;
    }

    public Bucket getUsersBucket() {
        if (Objects.nonNull(usersBucket)) {
            return usersBucket;
        } else {
            return openUsersBucket();
        }
    }

    public void setUsersBucket(Bucket usersBucket) {
        this.usersBucket = usersBucket;
    }
}
