package javaday.istanbul.sliconf.micro.dao;
/*
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.model.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
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
public class EventDao {
    /*

    private Logger logger = LogManager.getLogger(EventDao.class);

    public ResponseMessage saveEvent(Event event) {
        ResponseMessage message = new ResponseMessage(false, "An error occured while writing data", null);

        RawJsonDocument document = RawJsonDocument.create(event.getId(), JsonUtil.toJson(event));

        try {
            RawJsonDocument savedDocument = Main.couchBaseConfig.getEventsBucket().upsert(document);

            if (Objects.nonNull(savedDocument)) {
                message.setStatus(true);
                message.setMessage("Event saved successfully to DB!");
                message.setReturnObject(JsonUtil.fromJson(savedDocument.toString(), Event.class));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return message;
    }

    public Event getEvent(Event event) {
        Bucket bucket = Main.couchBaseConfig.getEventsBucket();

        Statement statement = select("*")
                .from(i("events"))
                .where(x("key").eq(x("$key")));

        JsonObject placeholderValues = JsonObject.create().put("key", event.getKey());
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);

        List<N1qlQueryRow> events = new ArrayList<>();

        try {
            events = bucket.query(q).allRows();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (!events.isEmpty()) {
            return JsonUtil.fromJson(events.get(0).value().getString("events"), Event.class);
        } else {
            return null;
        }
    }
    */
}
