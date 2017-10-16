package javaday.istanbul.sliconf.micro.controller.login;

import com.couchbase.client.java.document.json.JsonObject;
import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.UserPassService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.UserSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api
@Path("/service/users/update")
@Produces("application/json")
@Component
public class UpdateUserRouter implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;


    @Autowired
    public UpdateUserRouter(LoginControllerMessageProvider loginControllerMessageProvider,
                            UserRepositoryService userRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
    }

    @POST
    @ApiOperation(value = "Updates user with given parameters", nickname = "UpdateUserRoute")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header") //
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        // Todo @anilcosar burada swagger icin alinan parametreler belirtilmeli

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
}
