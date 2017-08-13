package javaday.istanbul.sliconf.micro.controller;


import javaday.istanbul.sliconf.micro.dao.UserDao;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserTemplateService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Objects;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;


/**
 * Created by ttayfur on 7/18/17.
 */
@Component
public class LoginController {

    //private final static Logger logger = LogManager.getLogger(LoginController.class);

    private final static UserDao userDao = new UserDao();


    private LoginControllerMessageProvider loginControllerMessageProvider;


    private UserRepositoryService userRepositoryService;

    private UserTemplateService userTemplateService;


    @Autowired
    public LoginController(LoginControllerMessageProvider loginControllerMessageProvider,
                           UserRepositoryService userRepositoryService,
                           UserTemplateService userTemplateService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.userTemplateService = userTemplateService;
    }

    public ResponseMessage test(Request request, Response response) {
        return new ResponseMessage(true, "selam talip", new Object());
    }


    public ResponseMessage createUser(Request request, Response response) {
        ResponseMessage responseMessage;

        String body = request.body();
        User user = JsonUtil.fromJson(body, User.class);

        List<User> dbUsers = userRepositoryService.findByName(user.getName());

        // eger user yoksa kayit et
        if (Objects.nonNull(dbUsers) && !dbUsers.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("userNameAlreadyUsed"), new Object());
            return responseMessage;
        }

        // user.generateId(); // id bos kalmasin dbye yazarken gerekli

        // Todo make a standalone class for writing to db
        UserPassService userPassService = new UserPassService();
        byte[] salt = userPassService.getSalt();
        byte[] hashedPassword = userPassService.getHashedPassword(user.getPassword(), salt);

        user.setSalt(salt);
        user.setHashedPassword(hashedPassword);

        user.setPassword("");

        // todo yazilip yazilmadigini kontrol et
        ResponseMessage dbResponse = userRepositoryService.save(user);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                loginControllerMessageProvider.getMessage("userSaveSuccessful"), user);

        return responseMessage;
    }


    public ResponseMessage loginUser(Request request, Response response) {
        String body = request.body();
        User requestUser = JsonUtil.fromJson(body, User.class);

        List<User> userList = userRepositoryService.findByName(requestUser.getName());

        if (Objects.nonNull(userList) && !userList.isEmpty()) {

            User dbUser = userList.get(0);

            if( Objects.nonNull(dbUser) && Objects.nonNull(dbUser.getHashedPassword()) && Objects.nonNull(dbUser.getSalt())) {
                UserPassService userService = new UserPassService();

                if (userService.checkIfUserAuthenticated(
                        requestUser.getPassword(), dbUser.getHashedPassword(), dbUser.getSalt())) {
                    return new ResponseMessage(true, "User successfully logged in", dbUser);
                }
            }



        }

        return new ResponseMessage(false, "Wrong user name or password", new Object());
    }


}
