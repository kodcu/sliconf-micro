package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.provider.LoginControllerMessageProvider;
import javaday.istanbul.sliconf.micro.model.ResponseError;
import javaday.istanbul.sliconf.micro.model.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.*;


/**
 * Created by ttayfur on 7/6/17.
 */
@Component
public class RootController {


    private LoginController loginController;


    private LoginControllerMessageProvider loginControllerMessageProvider;


    //private final LoginControllerMessageProvider loginControllerMessageProvider = LoginControllerMessageProvider.instance();

    @Autowired
    public RootController(LoginControllerMessageProvider loginControllerMessageProvider, LoginController loginController) {

        this.loginController = loginController;
        this.loginControllerMessageProvider = loginControllerMessageProvider;

        port(Constants.SERVER_PORT);

        before((request, response) -> {
            String token = request.queryParams("token");

            // ... check if authenticated
            if (!"auth".equals(token)) {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setStatus(false);
                responseMessage.setMessage(loginControllerMessageProvider.getMessage("notAuthenticated"));
                responseMessage.setReturnObject(new Object());
                halt(401, JsonUtil.toJson(responseMessage));
            }
        });

        // post("/service/users/register", LoginController::createUser, JsonUtil.json());


        path("/service", () -> {
            path("/users", () -> {
                //post("/login", loginController::loginUser, JsonUtil.json());

                // post("/register", loginController::createUser, JsonUtil.json());
                post("/test", loginController::test, JsonUtil.json());
            });

            path("/events", () -> {
                //post("/create-event", null, JsonUtil.json());
                //post("/list-event", null, JsonUtil.json());
                //post("/search-event", null, JsonUtil.json());
            });
        });

        /*
        get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            User user = userService.getUser(id);

            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("No user with id '%s' found", id);
        }, JsonUtil.json());
*/

        /*
        put("/users/:id", (req, res) -> userService.updateUser(
                req.params(":id"),
                req.queryParams("name"),
                req.queryParams("email")
        ), JsonUtil.json());
        */

        after((req, res) -> res.type("application/json"));

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            //res.body(JsonUtil.toJson(new ResponseError(e)));
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");

            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("The page you looking for not found!");
            responseMessage.setReturnObject(new Object());
            responseMessage.setStatus(false);

            return responseMessage;
        });
    }
}
