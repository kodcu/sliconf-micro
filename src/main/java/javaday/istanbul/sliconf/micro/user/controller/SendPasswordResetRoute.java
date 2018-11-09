package javaday.istanbul.sliconf.micro.user.controller;

import com.google.gson.JsonObject;
import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.service.PasswordResetService;
import javaday.istanbul.sliconf.micro.util.VerifyCaptcha;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.Objects;

@Api
@Path("/service/users/password-reset/send/:email")
@Produces("application/json")
@Component
public class SendPasswordResetRoute implements Route {

    private PasswordResetService passwordResetService;

    private Logger logger = LoggerFactory.getLogger(SendPasswordResetRoute.class);

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

        String userCaptchaString = "";
        String captchaString = "captcha";

        if (Objects.nonNull(captcha) &&
                Objects.nonNull(captcha.getAsJsonPrimitive(captchaString)) &&
                Objects.nonNull(captcha.getAsJsonPrimitive(captchaString).getAsString())) {
            userCaptchaString = captcha.getAsJsonPrimitive(captchaString).getAsString();
        }

        logger.info("Password reset started for user {}", email);


        if (Objects.nonNull(userCaptchaString) && !userCaptchaString.isEmpty() &&
                !isCaptchaValid(userCaptchaString)) {
            return new ResponseMessage(false, "Captcha is not valid", userCaptchaString);
        }

        return this.passwordResetService.sendPasswordResetMail(email);
    }

    private boolean isCaptchaValid(String userCaptcha) {
        boolean result = false;

        try {
            result = VerifyCaptcha.verify(userCaptcha);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }
}
