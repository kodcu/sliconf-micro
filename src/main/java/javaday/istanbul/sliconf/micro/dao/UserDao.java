package javaday.istanbul.sliconf.micro.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

/**
 * Created by ttayfur on 7/31/17.
 */
public class UserDao {

    private Logger logger = LogManager.getLogger(UserDao.class);

    public JsonObject getUser(User user) {
        Bucket bucket = Main.couchBaseConfig.getUsersBucket();

        Statement statement = select("*")
                .from(i("users"))
                .where(x("name").eq(x("$name")));

        JsonObject placeholderValues = JsonObject.create().put("name", user.getName());
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);

        List<N1qlQueryRow> users = new ArrayList<>();

        try {
            users = bucket.query(q).allRows();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (!users.isEmpty()) {
            return users.get(0).value();
        } else {
            return JsonObject.empty();
        }
    }

}
