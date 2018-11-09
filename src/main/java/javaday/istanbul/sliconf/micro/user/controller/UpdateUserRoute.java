package javaday.istanbul.sliconf.micro.user.controller;

import com.couchbase.client.java.document.json.JsonObject;
import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.user.service.UserPassService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.user.UserSpecs;
import javaday.istanbul.sliconf.micro.user.model.User;
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
@Path("/service/users/update")
@Produces("application/json")
@Component
public class UpdateUserRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;


    @Autowired
    public UpdateUserRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                           UserRepositoryService userRepositoryService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
    }

    @POST
    @ApiOperation(value = "Updates user with given parameters", nickname = "UpdateUserRoute")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"),//
            @ApiImplicitParam(required = true, dataTypeClass = User.class, paramType = "body") //

    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        String body = request.body();
        JsonObject updateParams = JsonObject.fromJson(body);

        return updateUser(updateParams);
    }

    public ResponseMessage updateUser(JsonObject updateParams) {

        String id = updateParams.getString("id");

        if (Objects.isNull(id)) {
            return new ResponseMessage(false, "User id can not be null!", new Object());
        }

        User user = userRepositoryService.findById(id).orElse(null);

        String stringUsername = "username";
        String stringFullname = "fullname";
        String stringPass = "password";
        String stringOldPass = "oldpassword";

        if (Objects.nonNull(user)) {
            boolean changed = false;
            if (Objects.nonNull(updateParams.getString(stringUsername))) {
                if (UserSpecs.checkUserParams(updateParams.getString(stringUsername), 4)) {

                    List<User> users = userRepositoryService.findByUsernameDiffrentThenId(updateParams.getString(stringUsername), id);

                    if (Objects.nonNull(users) && !users.isEmpty()) {

                        return new ResponseMessage(false, "Username already used by another user", updateParams.get("content"));
                    }

                    user.setUsername(updateParams.getString(stringUsername));
                    changed = true;
                } else
                    return new ResponseMessage(false, "New username must be at least 4", new Object());
            }
            if (Objects.nonNull(updateParams.getString(stringFullname))) {
                user.setFullname(updateParams.getString(stringFullname));
                changed = true;
            }
            if (Objects.nonNull(updateParams.getString(stringPass)) && Objects.nonNull(updateParams.getString(stringOldPass))) {
                UserPassService service = new UserPassService();
                if (service.checkPassword(updateParams.getString(stringOldPass), user.getHashedPassword(), user.getSalt())) {
                    if (!updateParams.getString(stringOldPass).equals(updateParams.getString(stringPass))) {
                        if (UserSpecs.checkUserParams(updateParams.getString(stringPass), 8)) {
                            user.setPassword(updateParams.getString(stringPass));
                            user = service.createNewUserWithHashedPassword(user);
                            changed = true;
                        } else
                            return new ResponseMessage(false, "New password must be at least 4", new Object());
                    } else
                        return new ResponseMessage(false, "New password and old password cannot be same", new Object());
                } else
                    return new ResponseMessage(false, "Wrong old Password", new Object());

            }

            if (changed) {
                ResponseMessage responseMessage = userRepositoryService.save(user);

                if (!responseMessage.isStatus()) {
                    return responseMessage;
                }

                return new ResponseMessage(true, "User successfully updated", responseMessage.getReturnObject());
            } else
                return new ResponseMessage(false, "Please check params", new Object());
        }

        return new ResponseMessage(false,
                loginControllerMessageProvider.getMessage("wrongUserNameOrPassword"), new Object());
    }
}
