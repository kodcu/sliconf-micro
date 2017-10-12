package javaday.istanbul.sliconf.micro.service.user;

import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.view.ViewQuery;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

@Service
@Profile("prod")
public class UserTemplateService implements UserService {

    private static final String DESIGN_DOC = "user";

    private CouchbaseTemplate template;

    @Autowired
    @Qualifier(value="usersTemplate")
    public void setCouchbaseTemplate(CouchbaseTemplate usersTemplate) {
        this.template = usersTemplate;
    }

    public User findOne(String id) {
        return template.findById(id, User.class);
    }

    public List<User> findAll() {
        return template.findByView(ViewQuery.from(DESIGN_DOC, "all"), User.class);
    }

    public List<User> findByName(String name) {
        Statement statement = select("*")
                .from(i("users"))
                .where(x("name").eq(x("$name")));
        JsonObject placeholderValues = JsonObject.create().put("name", name);
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);

        return template.findByN1QL(q, User.class);
    }

    public void delete(User user) {
        template.remove(user);
    }

    public ResponseMessage save(User user) {
        template.save(user);
        return null;
    }

    @Override
    public User findFirstByEmailEquals(String email) {
        return null;
    }
}