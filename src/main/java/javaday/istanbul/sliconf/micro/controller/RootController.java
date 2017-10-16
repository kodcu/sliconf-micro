package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.controller.event.CreateEventRouter;
import javaday.istanbul.sliconf.micro.controller.event.GetEventWithKeyRouter;
import javaday.istanbul.sliconf.micro.controller.event.ListEventsRoute;
import javaday.istanbul.sliconf.micro.controller.login.*;
import javaday.istanbul.sliconf.micro.model.response.ResponseError;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import javaday.istanbul.sliconf.micro.util.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static javaday.istanbul.sliconf.micro.SliconfMicroApp.APP_PACKAGE;
import static spark.Spark.*;


/**
 * Created by ttayfur on 7/6/17.
 */
@Component
public class RootController {


    // User related routes
    private static CreateUserRoute createUserRoute;
    private static LoginUserRoute loginUserRoute;
    private static SendPasswordResetRoute sendPasswordResetRoute;
    private static ResetPasswordRoute resetPasswordRoute;
    private static UpdateUserRouter updateUserRouter;

    // Event related routes
    private static CreateEventRouter createEventRouter;
    private static GetEventWithKeyRouter getEventWithKeyRouter;
    private static ListEventsRoute listEventsRoute;

    @Autowired
    public RootController(CreateUserRoute createUserRoute,
                          LoginUserRoute loginUserRoute,
                          SendPasswordResetRoute sendPasswordResetRoute,
                          ResetPasswordRoute resetPasswordRoute,
                          UpdateUserRouter updateUserRouter,
                          CreateEventRouter createEventRouter,
                          GetEventWithKeyRouter getEventWithKeyRouter,
                          ListEventsRoute listEventsRoute) {
        RootController.createUserRoute = createUserRoute;
        RootController.loginUserRoute = loginUserRoute;
        RootController.sendPasswordResetRoute = sendPasswordResetRoute;
        RootController.resetPasswordRoute = resetPasswordRoute;
        RootController.updateUserRouter = updateUserRouter;
        RootController.createEventRouter = createEventRouter;
        RootController.getEventWithKeyRouter = getEventWithKeyRouter;
        RootController.listEventsRoute = listEventsRoute;
    }

    public static void setPaths() {

        try {
            // Build swagger json description
            final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
            get("/swagger", (req, res) -> swaggerJson);

        } catch (Exception e) {
            // Todo use logger
            System.out.println(e);
        }

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
                post("login", loginUserRoute, JsonUtil.json());
                post("register", createUserRoute, JsonUtil.json());
                post("update", updateUserRouter, JsonUtil.json());
                post("password-reset/send/:email", sendPasswordResetRoute, JsonUtil.json());
                post("password-reset/reset/:token", resetPasswordRoute, JsonUtil.json());
            });

            path("events/", () -> {
                post("create/:userId", createEventRouter, JsonUtil.json());
                get("list/:userId", listEventsRoute, JsonUtil.json());

                path("/get/", () ->
                        get("with-key/:key", getEventWithKeyRouter, JsonUtil.json())
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
