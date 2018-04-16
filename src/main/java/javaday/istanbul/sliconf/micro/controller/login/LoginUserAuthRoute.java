package javaday.istanbul.sliconf.micro.controller.login;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.AuthUtil;
import javaday.istanbul.sliconf.micro.util.LoginTokenUtil;
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
import java.util.Objects;

@Api
@Path("/service/users/login/auth/:serviceName/:token")
@Produces("application/json")
@Component
public class LoginUserAuthRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;
    private UserRepositoryService userRepositoryService;
    private TokenAuthenticationService tokenAuthenticationService;

    private Logger logger = LoggerFactory.getLogger(LoginUserAuthRoute.class);

    @Autowired
    public LoginUserAuthRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                              UserRepositoryService userRepositoryService,
                              TokenAuthenticationService tokenAuthenticationService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @POST
    @ApiOperation(value = "Login end point for user", nickname = "LoginUserAuthRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "serviceName", paramType = "path", allowableValues = "google, linkedin"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String serviceName = request.params("serviceName");
        String token = request.params("token");

        User user;

        if (AuthUtil.SERVICE_GOOGLE.equalsIgnoreCase(serviceName)) {
            user = AuthUtil.loginGoogle(token);
        } else if (AuthUtil.SERVICE_LINKEDIN.equalsIgnoreCase(serviceName)) {
            user = AuthUtil.loginLinkedIn(token);
        } else {
            return new ResponseMessage(false, "Service name did not recognized", serviceName);
        }

        ResponseMessage responseMessage = loginUser(user);
        LoginTokenUtil.addAuthenticationTokenOnLogin(responseMessage, response, tokenAuthenticationService);

        return responseMessage;
    }

    /**
     * Eger kullanici maili ile sistem uzerinde kayitli ise kayitli kullanici doner<p>
     * Eger kayitli degilse yeni bir kullanici olusturulur
     * @param user
     * @return
     */
    public ResponseMessage loginUser(User user) {
        if (Objects.isNull(user)) {
            return new ResponseMessage(false, "Authentication token is not valid", "");
        }

        User dbUser = userRepositoryService.findByEmail(user.getEmail());

        if (Objects.nonNull(dbUser)) {
            dbUser.setPassword("");
            dbUser.setHashedPassword(null);
            dbUser.setSalt(null);
            return new ResponseMessage(true, "Successfully logged in!", dbUser);
        } else {
            userRepositoryService.save(user);
        }

        return new ResponseMessage(true, "", user);
    }
}
