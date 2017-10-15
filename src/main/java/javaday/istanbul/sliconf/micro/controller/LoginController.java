package javaday.istanbul.sliconf.micro.controller;


import com.couchbase.client.java.document.json.JsonObject;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.PasswordResetService;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.token.TokenRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.UserSpecs;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by ttayfur on 7/18/17.
 */
@Component
public class LoginController {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;

    private PasswordResetService passwordResetService;

    private TokenRepositoryService tokenRepositoryService;


    @Autowired
    public LoginController(LoginControllerMessageProvider loginControllerMessageProvider,
                           UserRepositoryService userRepositoryService,
                           PasswordResetService passwordResetService,
                           TokenRepositoryService tokenRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.passwordResetService = passwordResetService;
        this.tokenRepositoryService = tokenRepositoryService;
    }

    public ResponseMessage test(Request request, Response response) {
        return new ResponseMessage(true, "Hi, son of Earth!", new Object());
    }


    public ResponseMessage createUser(Request request, Response response) {
        ResponseMessage responseMessage;

        String body = request.body();
        User user = JsonUtil.fromJson(body, User.class);

        List<User> dbUsers = userRepositoryService.findByName(user.getName());

        // Todo mail validation yapilmali UserSpecs

        // eger user yoksa kayit et
        if (Objects.nonNull(dbUsers) && !dbUsers.isEmpty()) {
            if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
                responseMessage = new ResponseMessage(false,
                        loginControllerMessageProvider.getMessage("emailAlreadyUsed"), new Object());
                return responseMessage;
            }
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

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());
    }

    public ResponseMessage updateUser(Request request, Response response) {
        List<Boolean> validationList = new ArrayList<>();
        String body = request.body();
        JsonObject updateParams = JsonObject.fromJson(body);
        User user = userRepositoryService.findByEmail(updateParams.getString("email"));
        if (Objects.nonNull(user)) {
            if (Objects.nonNull(updateParams.getString("name"))) {
                if (UserSpecs.checkUserParams(updateParams.getString("name"), 4)) {
                    user.setName(updateParams.getString("name"));
                    validationList.add(true);
                } else
                    validationList.add(false);
            }
            if (Objects.nonNull(updateParams.getString("pass"))) {
                if (UserSpecs.checkUserParams(updateParams.getString("pass"), 4)) {
                    UserPassService userPassService = new UserPassService();
                    user.setPassword(updateParams.getString("pass"));
                    user = userPassService.createNewUserWithHashedPassword(user);
                } else
                    validationList.add(false);
            }

            // TODO: Update fonksiyonunu arastir.
            if (!validationList.contains(false)) {
                userRepositoryService.save(user);
                return new ResponseMessage(true, "User successfully updated", user);
            } else
                return new ResponseMessage(false,
                        loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());

        }

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());
    }

    public ResponseMessage sendPasswordReset(Request request, Response response) {

        String email = request.params("email");

        return this.passwordResetService.sendPasswordResetMail(email);
    }

    public ResponseMessage resetPassword(Request request, Response response) {

        String tokenValue = request.params("token");

        JsonObject passData = JsonObject.fromJson(request.body());

        ResponseMessage isTokenValidMessage = this.tokenRepositoryService.isTokenValid(tokenValue);

        if (Objects.nonNull(isTokenValidMessage) && isTokenValidMessage.isStatus()) {
            return userRepositoryService.changePassword((String) isTokenValidMessage.getReturnObject(),
                    passData.getString("pass"), passData.getString("repass"));
        } else {
            return isTokenValidMessage;
        }
    }
}
