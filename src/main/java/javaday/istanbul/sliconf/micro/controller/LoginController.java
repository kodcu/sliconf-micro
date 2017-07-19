package javaday.istanbul.sliconf.micro.controller;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.service.UserService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import spark.Request;
import spark.Response;

import java.util.Map;


/**
 * Created by ttayfur on 7/18/17.
 */
public class LoginController {

    public static Object createUser(Request request, Response response) {
        String body = request.body();
        User user = (User) JsonUtil.fromJson(body);
        user.generateId(); // id bos kalmasin dbye yazarken gerekli


        // Todo make a standalone class for writing to db
        UserService userService = new UserService();
        byte[] salt = userService.getSalt();
        byte[] hashedPassword = userService.getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");
        saveToDB(user);
        return user;
    }

    private static void saveToDB(User user) {
        JsonObject userJSON = JsonObject.create();

        Map<String,Object> userMap = JsonUtil.mapFromObject(user);

        userMap.forEach(userJSON::put);

        // Todo id kismi uniqe olmali
        JsonDocument document = JsonDocument.create(user.getId(), userJSON);

        Main.couchBaseConfig.getUsersBucket().upsert(document);
    }
}
