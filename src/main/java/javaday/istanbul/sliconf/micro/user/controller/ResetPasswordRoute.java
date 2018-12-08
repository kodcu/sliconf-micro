package javaday.istanbul.sliconf.micro.user.controller;

import com.couchbase.client.java.document.json.JsonObject;
import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.token.Token;
import javaday.istanbul.sliconf.micro.security.token.TokenRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;

@Api(value = "user", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/users/password-reset/reset/:token")
@Produces("application/json")
@Component
public class ResetPasswordRoute implements Route {

    private UserRepositoryService userRepositoryService;
    private TokenRepositoryService tokenRepositoryService;


    @Autowired
    public ResetPasswordRoute(UserRepositoryService userRepositoryService,
                              TokenRepositoryService tokenRepositoryService) {
        this.userRepositoryService = userRepositoryService;
        this.tokenRepositoryService = tokenRepositoryService;
    }

    @POST
    @ApiOperation(value = "Reset password with given token", nickname = "ResetPasswordRoute")
    @ApiImplicitParams({
            @ApiImplicitParam(required = true, dataType = "string", name = ":token", paramType = "path") //
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String tokenValue = request.params("token");

        JsonObject passData = JsonObject.fromJson(request.body());

        ResponseMessage isTokenValidMessage = this.tokenRepositoryService.isTokenValid(tokenValue);

        if (Objects.nonNull(isTokenValidMessage) && isTokenValidMessage.isStatus()) {

            ResponseMessage changeResponseMessage = userRepositoryService.changePassword((String) isTokenValidMessage.getReturnObject(),
                    passData.getString("pass"), passData.getString("repass"));


            if (Objects.nonNull(changeResponseMessage) && changeResponseMessage.isStatus()) {
                Token token = tokenRepositoryService.findTokenByTokenValueEquals(tokenValue);
                tokenRepositoryService.remove(token);
            }

            return changeResponseMessage;
        } else {
            return isTokenValidMessage;
        }
    }
}
