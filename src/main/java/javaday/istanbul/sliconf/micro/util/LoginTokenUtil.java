package javaday.istanbul.sliconf.micro.util;

import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.user.model.User;
import spark.Response;

import java.util.Objects;

public class LoginTokenUtil {

    private LoginTokenUtil() {
        // Private constructor for static access
    }

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
