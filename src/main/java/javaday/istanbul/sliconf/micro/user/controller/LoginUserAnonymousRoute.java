package javaday.istanbul.sliconf.micro.user.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.user.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.LoginTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;

@Api
@Path("/service/users/login/anonymous/:deviceId")
@Produces("application/json")
@Component
public class LoginUserAnonymousRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;
    private UserRepositoryService userRepositoryService;
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    public LoginUserAnonymousRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                                   UserRepositoryService userRepositoryService,
                                   TokenAuthenticationService tokenAuthenticationService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @POST
    @ApiOperation(value = "Login end point for anonymous user", nickname = "LoginUserAnonymousRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "deviceId", paramType = "path") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String deviceId = request.params("deviceId");

        if (Objects.isNull(deviceId) || deviceId.isEmpty()) {
            return new ResponseMessage(false, "Device id can not be empty or null!", deviceId);
        }

        ResponseMessage responseMessage = getUserWithDeviceId(deviceId);

        LoginTokenUtil.addAuthenticationTokenOnLogin(responseMessage, response, tokenAuthenticationService);

        return responseMessage;
    }

    public ResponseMessage getUserWithDeviceId(String deviceId) {
        List<User> userList;

        userList = userRepositoryService.findByDeviceId(deviceId);

        if (Objects.nonNull(userList) && !userList.isEmpty()) {

            User dbUser = userList.get(0);

            if (Objects.nonNull(dbUser)) {
                dbUser.setHashedPassword(null);
                dbUser.setSalt(null);

                return new ResponseMessage(true, "User with given device id!", dbUser);
            }
        }

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("userNotFoundWithGivenDeviceId"), deviceId);
    }
}
