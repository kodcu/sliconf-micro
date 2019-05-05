package javaday.istanbul.sliconf.micro.user.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.security.TokenAuthenticationService;
import javaday.istanbul.sliconf.micro.user.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.util.EmailUtil;
import javaday.istanbul.sliconf.micro.util.LoginTokenUtil;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(value = "user", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/users/register")
@Produces("application/json")
@Component
public class CreateUserRoute implements Route {

    private LoginControllerMessageProvider loginControllerMessageProvider;

    private UserRepositoryService userRepositoryService;

    private TokenAuthenticationService tokenAuthenticationService;


    @Autowired
    public CreateUserRoute(LoginControllerMessageProvider loginControllerMessageProvider,
                           UserRepositoryService userRepositoryService,
                           TokenAuthenticationService tokenAuthenticationService) {
        this.loginControllerMessageProvider = loginControllerMessageProvider;
        this.userRepositoryService = userRepositoryService;
        this.tokenAuthenticationService = tokenAuthenticationService;
    }


    @POST
    @ApiOperation(value = "Creates a new user", nickname = "CreateUserRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataTypeClass = User.class, paramType = "body") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        String body = request.body();
        System.out.println("-->  request.body " + body);
        User user = JsonUtil.fromJson(body, User.class);

        // anonim olarak giris yapmis kullanicinin deviceid sini alip yeni olusacak hesaba ekliyoruz.
        // boylece kullaniciyi unique kullanicilar listesine iki defa eklemiyoruz.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication.getCredentials())  && !authentication.getCredentials().toString().isEmpty()) {
            // mobil den register oluyor.
            Map<String, String> userMap = ((Map) authentication.getCredentials());
            user.setDeviceId(userMap.get("deviceId"));
        }




        ResponseMessage responseMessage = registerUser(user);

        LoginTokenUtil.addAuthenticationTokenOnLogin(responseMessage, response, tokenAuthenticationService);

        return responseMessage;
    }

    public ResponseMessage registerUser(User user) {
        ResponseMessage responseMessage;
        System.out.println(" --> " + user);

        if (Objects.isNull(user)) {
            responseMessage = new ResponseMessage(false, "User can not be empty", "");
            return responseMessage;
        }

        System.out.println(" --> " + user.getEmail());
        if (!EmailUtil.validateEmail(user.getEmail())) {
            responseMessage = new ResponseMessage(false, "Email is not valid", user.getEmail());
            System.out.println(" -->  Email is not valid  " );
            return responseMessage;
        }

        /**
        List<User> dbUsers = userRepositoryService.findByUsername(user.getUsername());

        // eger user yoksa kayit et
        if (Objects.nonNull(dbUsers) && !dbUsers.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("userNameAlreadyUsed"), new Object());
            return responseMessage;
        }

         */

        if (userRepositoryService.controlIfEmailIsExists(user.getEmail())) {
            responseMessage = new ResponseMessage(false,
                    loginControllerMessageProvider.getMessage("emailAlreadyUsed"), new Object());
            System.out.println(" -->  emailAlreadyUsed  "  + user.getEmail());
            return responseMessage;
        }

        System.out.println(" -->  before save "  + user);

        ResponseMessage dbResponse = userRepositoryService.saveUser(user);

        System.out.println(" -->  after save "  + user);

        if (!dbResponse.isStatus()) {
            System.out.println(" -->  !dbResponse.isStatus() "  + !dbResponse.isStatus());
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                loginControllerMessageProvider.getMessage("userSaveSuccessful"), dbResponse.getReturnObject());

        System.out.println(" --> responseMessage "  + responseMessage);

        return responseMessage;
    }

}
