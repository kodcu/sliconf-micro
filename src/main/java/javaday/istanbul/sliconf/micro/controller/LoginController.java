package javaday.istanbul.sliconf.micro.controller;


import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserTemplateService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Objects;


/**
 * Created by ttayfur on 7/18/17.
 */
@Component
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

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


        UserPassService userPassService = new UserPassService();
        User saltedUser = userPassService.createNewUserWithHashedPassword(user);

        ResponseMessage dbResponse = userRepositoryService.save(saltedUser);

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

            if (Objects.nonNull(dbUser) && Objects.nonNull(dbUser.getHashedPassword()) && Objects.nonNull(dbUser.getSalt())) {
                UserPassService userService = new UserPassService();

                if (userService.checkIfUserAuthenticated(dbUser, requestUser)) {
                    dbUser.setHashedPassword(null);
                    dbUser.setSalt(null);

                    return new ResponseMessage(true, "User successfully logged in", dbUser);
                }
            }
        }

        return new ResponseMessage(false, "Wrong user name or password", new Object());
    }
}
