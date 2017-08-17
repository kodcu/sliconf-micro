package javaday.istanbul.sliconf.micro.service.event;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.view.ViewQuery;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

@Service
@Qualifier("EventTemplateService")
public class EventTemplateService implements EventService {

    private static final String DESIGN_DOC = "event";

    private CouchbaseTemplate template;

    @Autowired
    public void setCouchbaseTemplate(@Qualifier("eventsTemplate") CouchbaseTemplate eventsTemplate) {
        this.template = eventsTemplate;
    }

    public Event findOne(String id) {
        return template.findById(id, Event.class);
    }

    public List<Event> findAll() {
        return template.findByView(ViewQuery.from(DESIGN_DOC, "all"), Event.class);
    }

    public List<Event> findByName(String name) {
        Statement statement = select("*")
                .from(i("events"))
                .where(x("name").eq(x("$name")));
        JsonObject placeholderValues = JsonObject.create().put("name", name);
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);

        return template.findByN1QL(q, Event.class);
    }

    public void delete(Event event) {
        template.remove(event);
    }

    public ResponseMessage save(Event event) {
        template.save(event);
        return null;
    }
}