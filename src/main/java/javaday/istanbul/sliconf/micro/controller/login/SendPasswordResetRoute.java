package javaday.istanbul.sliconf.micro.controller.login;

import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.PasswordResetService;
import javaday.istanbul.sliconf.micro.util.VerifyCaptcha;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Api
@Path("/service/users/password-reset/send/:email")
@Produces("application/json")
@Component
public class SendPasswordResetRoute implements Route {

    private PasswordResetService passwordResetService;

    @Autowired
    public SendPasswordResetRoute(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @POST
    @ApiOperation(value = "Password Reset end point", nickname = "SendPasswordResetRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = ":email", paramType = "path") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String email = request.params("email");

        JsonObject captcha = JsonUtil.fromJson(request.body(), JsonObject.class);

        if (!VerifyCaptcha.verify(captcha.getAsJsonPrimitive("captcha").getAsString())) {
            return new ResponseMessage(false, "Captcha is not valid", captcha);
        }

        return this.passwordResetService.sendPasswordResetMail(email);
    }
}
