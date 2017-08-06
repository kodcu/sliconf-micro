package javaday.istanbul.sliconf.micro.controller;


import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;
import javaday.istanbul.sliconf.micro.Main;
import javaday.istanbul.sliconf.micro.dao.UserDao;
import javaday.istanbul.sliconf.micro.model.ResponseMessage;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import java.util.Objects;


/**
 * Created by ttayfur on 7/18/17.
 */
public class LoginController {

    private final static Logger logger = LogManager.getLogger(LoginController.class);

    private final static UserDao userDao = new UserDao();

    private final LoginControllerMessageProvider loginControllerMessageProvider = LoginControllerMessageProvider.instance();


    public ResponseMessage createUser(Request request, Response response) {
        ResponseMessage responseMessage = new ResponseMessage();

        String body = request.body();
        User user = (User) JsonUtil.fromJson(body);

        // eger user yoksa kayit et
        if (!userDao.getUser(user).isEmpty()) {
            responseMessage.setStatus(false);
            // Todo mesajlari properties dosyalarindan oku
            responseMessage.setMessage(loginControllerMessageProvider.getMessage("userNameAlreadyUsed"));
            responseMessage.setReturnObject(new Object());
            return responseMessage;
        }

        // Todo unique id random olmamali belirli bir kurali olmali
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
        responseMessage.setMessage(loginControllerMessageProvider.getMessage("userSaveSuccessful"));
        responseMessage.setReturnObject(user);

        return responseMessage;
    }

    public boolean loginUser(Request request, Response response) {
        String body = request.body();
        User requestUser = (User) JsonUtil.fromJson(body);

        JsonObject jsonObject = userDao.getUser(requestUser);

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
        RawJsonDocument document1 = RawJsonDocument.create(user.getId(), new Gson().toJson(user));
        Main.couchBaseConfig.getUsersBucket().upsert(document1);

    }
}
