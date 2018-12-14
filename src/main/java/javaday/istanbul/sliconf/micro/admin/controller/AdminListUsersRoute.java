package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.user.util.UserHelper;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;


@Api
@Path("/service/admin/list/users")
@Produces("application/json")
@Component
@AllArgsConstructor
public class AdminListUsersRoute implements Route {

    private final UserRepositoryService userRepositoryService;
    private final UserHelper userHelper;

    @POST
    @ApiOperation(value = "Lists users for admin", nickname = "AdminListUsersRoute")
    @ApiImplicitParams({})
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        return getUsers();
    }

    public ResponseMessage getUsers() {

        ResponseMessage responseMessage = userHelper.checkUserRoleIs(Constants.ROLE_ADMIN);

        if(!responseMessage.isStatus())
            return responseMessage;

        List<User> users = userRepositoryService.findAllByAnonymous();

        return new ResponseMessage(true, "Users listed", users);
    }
}
