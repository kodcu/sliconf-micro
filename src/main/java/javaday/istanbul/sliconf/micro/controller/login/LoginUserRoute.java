package javaday.istanbul.sliconf.micro.controller.login;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.UserCaptcha;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.EmailUtil;
import javaday.istanbul.sliconf.micro.util.LoginTokenUtil;
import javaday.istanbul.sliconf.micro.util.VerifyCaptcha;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Api
@Path("/service/users/login")
@Produces("application/json")
@Component
public class LoginUserRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;
    private UserRepositoryService userRepositoryService;
    private TokenAuthenticationService tokenAuthenticationService;

    private Logger logger = LoggerFactory.getLogger(LoginUserRoute.class);

    private String pwString = "password";

    @Autowired
    public LoginUserRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                          UserRepositoryService userRepositoryService,
                          TokenAuthenticationService tokenAuthenticationService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @POST
    @ApiOperation(value = "Login end point for user", nickname = "LoginUserRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataTypeClass = User.class, paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) &&
                Objects.nonNull(authentication.getCredentials()) &&
                authentication.getCredentials() instanceof LinkedHashMap) {

            LinkedHashMap<String, Object> authUser = (LinkedHashMap) authentication.getCredentials();

            authUser.put("salt", null);
            authUser.put("hashedPassword", null);
            authUser.put(pwString, null);

            return new ResponseMessage(true, "User Authenticated", authUser);
        }

        String body = request.body();
        UserCaptcha userCaptcha = JsonUtil.fromJson(body, UserCaptcha.class);

        if (Objects.nonNull(userCaptcha.getCaptcha()) && !userCaptcha.getCaptcha().isEmpty() &&
                !isCaptchaValid(userCaptcha)) {
            return new ResponseMessage(false, "Captcha is not valid", userCaptcha);
        }

        User requestUser = new User();

        requestUser.setPassword(userCaptcha.getPassword());
        requestUser.setUsername(userCaptcha.getUsername());

        ResponseMessage responseMessage = loginUser(requestUser);

        LoginTokenUtil.addAuthenticationTokenOnLogin(responseMessage, response, tokenAuthenticationService);

        return responseMessage;
    }

    private boolean isCaptchaValid(UserCaptcha userCaptcha) {
        boolean result = false;

        try {
            result = VerifyCaptcha.verify(userCaptcha.getCaptcha());
        } catch (IOException e) {
            this.logger.error(e.getMessage(), e);
        }

        return result;
    }

    public ResponseMessage loginUser(User requestUser) {

        List<User> userList;

        if (EmailUtil.validateEmail(requestUser.getUsername())) {
            userList = userRepositoryService.findUsersByEmail(requestUser.getUsername());
        } else {
            userList = userRepositoryService.findByUsername(requestUser.getUsername());
        }

        if (Objects.nonNull(userList) && !userList.isEmpty()) {

            User dbUser = userList.get(0);

            if (Objects.nonNull(dbUser) && Objects.nonNull(dbUser.getHashedPassword()) && Objects.nonNull(dbUser.getSalt())) {
                UserPassService userService = new UserPassService();

                if (userService.checkIfUserAuthenticated(dbUser, requestUser)) {
                    User responseUser = new User(dbUser);
                    responseUser.setHashedPassword(null);
                    responseUser.setSalt(null);

                    if (Objects.isNull(responseUser.getRole())) {
                        responseUser.setRole("ROLE_USER");
                    }

                    logger.info("User logged successfully %s", responseUser.getUsername());
                    return new ResponseMessage(true, "User successfully logged in", responseUser);
                }
            }
        }

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());
    }
}
