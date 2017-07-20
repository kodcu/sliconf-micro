package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.model.ResponseError;
import javaday.istanbul.sliconf.micro.util.JsonUtil;

import static spark.Spark.*;


/**
 * Created by ttayfur on 7/6/17.
 */
public class RootController {
    public RootController() {

        post("/service/users/register", LoginController::createUser, JsonUtil.json());
        post("/service/users/login", LoginController::loginUser, JsonUtil.json());

        // get("/users", (req, res) -> userService.getAllUsers(), JsonUtil.json());

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

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(JsonUtil.toJson(new ResponseError(e)));
        });
    }
}
