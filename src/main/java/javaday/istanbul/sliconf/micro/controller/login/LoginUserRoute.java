package javaday.istanbul.sliconf.micro.controller.login;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.EmailUtil;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
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
@Path("/service/users/login")
@Produces("application/json")
@Component
public class LoginUserRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;
    private UserRepositoryService userRepositoryService;


    @Autowired
    public LoginUserRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                          UserRepositoryService userRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
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
        String body = request.body();
        User requestUser = JsonUtil.fromJson(body, User.class);

        return loginUser(requestUser);
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
                    dbUser.setHashedPassword(null);
                    dbUser.setSalt(null);

                    return new ResponseMessage(true, "User successfully logged in", dbUser);
                }
            }
        }

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());
    }
}
