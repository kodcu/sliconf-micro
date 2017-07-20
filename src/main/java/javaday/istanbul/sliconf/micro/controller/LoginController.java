package javaday.istanbul.sliconf.micro.controller;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.model.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.service.UserService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;


/**
 * Created by ttayfur on 7/18/17.
 */
public class LoginController {

    static Logger logger = LogManager.getLogger(LoginController.class);

    public static ResponseMessage createUser(Request request, Response response) {
        ResponseMessage responseMessage = new ResponseMessage();

        String body = request.body();
        User user = (User) JsonUtil.fromJson(body);

        // eger user yoksa kayit et
        if (!getUserDB(user).isEmpty()) {
            responseMessage.setStatus(false);
            // Todo mesajlari properties dosyalarindan oku
            responseMessage.setMessage("Bu isimde bir kulanici bulunmakta");
            responseMessage.setReturnObject(new Object());
            return responseMessage;
        }

        user.generateId(); // id bos kalmasin dbye yazarken gerekli

        // Todo make a standalone class for writing to db
        UserService userService = new UserService();
        byte[] salt = userService.getSalt();
        byte[] hashedPassword = userService.getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");

        // todo yazilip yazilmadigini kontrol et
        saveUserDB(user);

        user.setHashedPassword(null);
        user.setSalt(null);

        responseMessage.setStatus(true);
        responseMessage.setMessage("Kullanici basariyla kaydedildi");
        responseMessage.setReturnObject(user);

        return responseMessage;
    }

    public static boolean loginUser(Request request, Response response) {
        String body = request.body();
        User requestUser = (User) JsonUtil.fromJson(body);

        JsonObject jsonObject = getUserDB(requestUser);

        if (!jsonObject.isEmpty()) {
            User userFromDB = (User) JsonUtil.fromJson(jsonObject.get("users").toString());

            if (Objects.nonNull(userFromDB) &&
                    Objects.nonNull(userFromDB.getHashedPassword()) &&
                    Objects.nonNull(userFromDB.getSalt())) {
                UserService userService = new UserService();

                return userService.checkIfUserAuthenticated(requestUser.getPassword(), userFromDB.getHashedPassword(), userFromDB.getSalt());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static void saveUserDB(User user) {
        JsonObject userJSON = JsonObject.create();

        Map<String, Object> userMap = JsonUtil.mapFromObject(user);

        userMap.forEach((key, value) -> {
            if (value instanceof ArrayList) {
                JsonArray arrayJSON = JsonArray.create();
                ((ArrayList) value).forEach(arrayJSON::add);
                userJSON.put(key, arrayJSON);
            } else {
                userJSON.put(key, value);
            }
        });

        // Todo id kismi uniqe olmali
        JsonDocument document = JsonDocument.create(user.getId(), userJSON);

        Main.couchBaseConfig.getUsersBucket().upsert(document);
    }

    private static JsonObject getUserDB(User user) {

        Bucket bucket = Main.couchBaseConfig.getUsersBucket();

        Statement statement = select("*")
                .from(i("users"))
                .where(x("name").eq(x("$name")));

        JsonObject placeholderValues = JsonObject.create().put("name", user.getName());
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);

        List<N1qlQueryRow> users;

        try {
            users = bucket.query(q).allRows();
        } catch (Exception e) {
            users = new ArrayList<>();
            logger.error(e.getMessage(), e);
        }

        if (!users.isEmpty()) {
            return users.get(0).value();
        } else {
            return JsonObject.empty();
        }
    }
}
