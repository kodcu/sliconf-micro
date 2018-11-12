package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Api(value = "admin", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/admin/users/:userId")
@Produces("application/json")
@Component
public class AdminGetUserInfo implements Route {

    private final UserRepositoryService userRepositoryService;


    @GET
    @ApiOperation(value = "Gets a specific user info for admin", nickname = "AdminGetUserInfoRoute")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"),
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"),
    })
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })

    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = request.params("userId");
        return getUser(authentication, userId);
    }

    public ResponseMessage getUser(Authentication authentication, String userId) {

        if (Objects.isNull(authentication)) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Constants.ROLE_ADMIN))) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        Optional<User> userOptional = userRepositoryService.findById(userId);
        return userOptional
                .map(user -> {
                    user.setPassword("");
                    user.setHashedPassword(new byte[0]);
                    user.setSalt(new byte[0]);
                    return new ResponseMessage(true, "User info has fetched", user);
                })
                .orElseGet(() -> new ResponseMessage(false, "User can not found by given id", userId));
    }
}
