package javaday.istanbul.sliconf.micro.controller.login;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.EmailUtil;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
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
@Path("/service/users/register")
@Produces("application/json")
@Component
public class CreateUserRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;


    @Autowired
    public CreateUserRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                           UserRepositoryService userRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
    }


    @POST
    @ApiOperation(value = "Creates a new user", nickname = "CreateUserRoute")
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
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        ResponseMessage responseMessage;

        String body = request.body();
        User user = JsonUtil.fromJson(body, User.class);

        if (Objects.isNull(user)) {
            responseMessage = new ResponseMessage(false, "User can not be empty", "");
            return responseMessage;
        }

        if (!EmailUtil.validateEmail(user.getEmail())) {
            responseMessage = new ResponseMessage(false, "Email is not valid", user.getEmail());
            return responseMessage;
        }

        List<User> dbUsers = userRepositoryService.findByUsername(user.getUsername());

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

        ResponseMessage dbResponse = userRepositoryService.saveUser(user);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                loginControllerMessageProvider.getMessage("userSaveSuccessful"), dbResponse.getReturnObject());

        return responseMessage;
    }

}
