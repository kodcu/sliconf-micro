package javaday.istanbul.sliconf.micro.dao;
/*
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import com.google.gson.Gson;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.model.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;
*/
/**
 * Created by ttayfur on 7/31/17.
 */
public class UserDao {
    /*

    private Logger logger = LogManager.getLogger(UserDao.class);

    public User getUser(User user) {
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
            return JsonUtil.fromJson(users.get(0).value().getString("users"), User.class);
        } else {
            return null;
        }
    }

    public ResponseMessage saveUser(User user) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while writing data", null);

        RawJsonDocument document = RawJsonDocument.create(user.getId(), JsonUtil.toJson(user));

        try {
            RawJsonDocument savedDocument = Main.couchBaseConfig.getUsersBucket().upsert(document);

            if(Objects.nonNull(savedDocument)) {
                message.setStatus(true);
                message.setMessage("User saved successfully to DB!");
                message.setReturnObject(JsonUtil.fromJson(savedDocument.toString(), User.class));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }
    */

}
