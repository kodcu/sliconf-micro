package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.controller.event.EventController;
import javaday.istanbul.sliconf.micro.model.response.ResponseError;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.*;


/**
 * Created by ttayfur on 7/6/17.
 */
@Component
public class RootController {

    private static LoginController loginController;
    private static EventController eventController;

    @Autowired
    public RootController(LoginController loginController, EventController eventController) {
        RootController.loginController = loginController;
        RootController.eventController = eventController;


    }

    public static void setPaths() {

        before((request, response) -> {

            // Todo auth sistemini devreye alinca kullan
            /*
            String token = request.queryParams("token");

            // ... check if authenticated
            if (!"auth".equals(token)) {
                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setStatus(false);
                responseMessage.setMessage(loginControllerMessageProvider.getMessage("notAuthenticated"));
                responseMessage.setReturnObject(new Object());
                halt(401, JsonUtil.toJson(responseMessage));
            }
            */
        });

        path("/service/", () -> {
            path("users/", () -> {
                post("login", loginController::loginUser, JsonUtil.json());
                post("register", loginController::createUser, JsonUtil.json());
                post("test", loginController::test, JsonUtil.json());
                post("update", loginController::updateUser, JsonUtil.json());
                post("password-reset/send/:email", loginController::sendPasswordReset, JsonUtil.json());
                post("password-reset/reset/:token", loginController::resetPassword, JsonUtil.json());
            });

            path("events/", () -> {
                post("create/:userId", eventController::createEvent, JsonUtil.json());
                post("list/:userId", eventController::listEvents, JsonUtil.json());
                post("search", null, JsonUtil.json());

                path("/get/", () ->
                        post("with-key/:key", eventController::getEventWithKey, JsonUtil.json())
                );
            });
        });

        after((req, res) -> res.type("application/json"));

        // Todo spark-java ile exception handling yapabilir miyiz?

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(JsonUtil.toJson(new ResponseError(e)));
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
