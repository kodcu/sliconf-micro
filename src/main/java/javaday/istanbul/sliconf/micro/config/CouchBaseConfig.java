package javaday.istanbul.sliconf.micro.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.search.SearchQuery;


/**
 * Created by ttayfur on 7/8/17.
 */
public class CouchBaseConfig {

    private String HOST_NAME = "couchbase://127.0.0.1";
    private String BUCKET_NAME = "sliconf";
    private String BUCKET_PASS = "jfFMd8Nd";

    public void createCouchBase() {
        Cluster cluster = CouchbaseCluster.create(HOST_NAME);
        Bucket bucket = cluster.openBucket(BUCKET_NAME, BUCKET_PASS);

        JsonObject user = JsonObject.create();

        user.put("firstname", "Talip");
        user.put("lastname", "Tayfur");
        user.put("username", "taifuru");
        user.put("password", "12345");

        JsonDocument document = JsonDocument.create("taliptayfur", user);

        bucket.upsert(document);

        System.out.println(bucket.get("taliptayfur").content());

    }
}
