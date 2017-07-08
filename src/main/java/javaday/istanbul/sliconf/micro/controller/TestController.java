package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.model.ResponseError;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.service.UserService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import spark.Spark;


/**
 * Created by ttayfur on 7/6/17.
 */
public class TestController {
    public TestController(final UserService userService) {

        Spark.get("/users", (req, res) -> userService.getAllUsers(), JsonUtil.json());

        Spark.get("/users/:id", (req, res) -> {
            String id = req.params(":id");

            User user = userService.getUser(id);

            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("No user with id '%s' found", id);
        }, JsonUtil.json());

        Spark.post("/users", (req, res) -> userService.createUser(
                req.queryParams("name"),
                req.queryParams("email")
        ), JsonUtil.json());

        Spark.put("/users/:id", (req, res) -> userService.updateUser(
                req.params(":id"),
                req.queryParams("name"),
                req.queryParams("email")
        ), JsonUtil.json());

        Spark.after((req, res) -> {
            res.type("application/json");
        });

        Spark.exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(JsonUtil.toJson(new ResponseError(e)));
        });
    }
}
