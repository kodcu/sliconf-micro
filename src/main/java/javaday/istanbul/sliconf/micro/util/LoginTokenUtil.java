package javaday.istanbul.sliconf.micro.util;

import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import spark.Response;

import java.util.Objects;

public class LoginTokenUtil {

    public static void addAuthenticationTokenOnLogin(ResponseMessage responseMessage, Response response, TokenAuthenticationService tokenAuthenticationService) {
        if (Objects.nonNull(responseMessage) && responseMessage.isStatus() &&
                Objects.nonNull(responseMessage.getReturnObject()) &&
                responseMessage.getReturnObject() instanceof User) {
            User user = (User) responseMessage.getReturnObject();
            String token = tokenAuthenticationService.addAuthentication(response.raw(), user);
            user.setToken(token);
            responseMessage.setReturnObject(user);
        }
    }
}
