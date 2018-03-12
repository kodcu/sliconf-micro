package javaday.istanbul.sliconf.micro.controller.login;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
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
@Path("/service/users/register/anonymous/:deviceId")
@Produces("application/json")
@Component
public class CreateUserAnonymousRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;


    @Autowired
    public CreateUserAnonymousRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                                    UserRepositoryService userRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
    }


    @POST
    @ApiOperation(value = "Creates a new anonymous user", nickname = "CreateUserAnonymousRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "deviceId", paramType = "path"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String deviceId = request.params("deviceId");

        return createAnonymousUser(deviceId);
    }

    public ResponseMessage createAnonymousUser(String deviceId) {
        ResponseMessage responseMessage;

        if (Objects.isNull(deviceId) || deviceId.isEmpty()) {
            responseMessage = new ResponseMessage(false, "Device id can not be empty or null!", "");
            return responseMessage;
        }

        String email = deviceId + "@example.com";

        User user = new User();

        user.setEmail(email);
        user.setUsername("anonymous-" + deviceId);
        user.setFullname("Anonymous");
        user.setAnonymous(true);
        user.setDeviceId(deviceId);

        List<User> dbUsers = userRepositoryService.findByDeviceId(user.getDeviceId());

        // eger user yoksa kayit et
        if (Objects.nonNull(dbUsers) && !dbUsers.isEmpty()) {

            User responseUser = dbUsers.get(0);

            if (Objects.nonNull(responseUser)) {
                responseUser.setPassword("");
                responseUser.setHashedPassword(null);
                responseUser.setSalt(null);
            }

            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("deviceIdAlreadyUsed"), responseUser);
            return responseMessage;
        }

        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("emailAlreadyUsed"), new Object());
            return responseMessage;
        }

        ResponseMessage dbResponse = userRepositoryService.saveAnonymousUser(user);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                loginControllerMessageProvider.getMessage("userAnonymousSaveSuccessfully"), dbResponse.getReturnObject());

        return responseMessage;
    }
}
