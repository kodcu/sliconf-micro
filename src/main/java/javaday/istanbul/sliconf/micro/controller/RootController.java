package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.model.response.ResponseError;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import javaday.istanbul.sliconf.micro.util.SwaggerParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static javaday.istanbul.sliconf.micro.SliconfMicroApp.APP_PACKAGE;
import static spark.Spark.*;


/**
 * Created by ttayfur on 7/6/17.
 */
@Component
public class RootController {

    private static RouteObjects routeObjects;

    @Autowired
    public RootController(RouteObjects routeObjects) {
        RootController.routeObjects = routeObjects;
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

        // Todo spark-java ile exception handling yapabilir miyiz?

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST.value());
            res.body(JsonUtil.toJson(new ResponseError(e)));
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");

            res.status(HttpStatus.NOT_FOUND.value());

            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("The page you looking for not found!");
            responseMessage.setReturnObject(req.url());
            responseMessage.setStatus(false);

            return JsonUtil.toJson(responseMessage);
        });

        before((request, response) -> {

            String token = request.headers("token");

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
                post("login", routeObjects.loginUserRoute, JsonUtil.json());
                post("register", routeObjects.createUserRoute, JsonUtil.json());
                post("update", routeObjects.updateUserRoute, JsonUtil.json());
                post("password-reset/send/:email", routeObjects.sendPasswordResetRoute, JsonUtil.json());
                post("password-reset/reset/:token", routeObjects.resetPasswordRoute, JsonUtil.json());
            });

            path("events/", () -> {
                post("create/:userId", routeObjects.createEventRoute, JsonUtil.json());
                get("list/:userId", routeObjects.listEventsRoute, JsonUtil.json());

                path("get/", () ->
                        get("with-key/:key", routeObjects.getEventWithKeyRoute, JsonUtil.json())
                );

                before();

                path("sponsor-tag/", () -> {
                    post("create/:event-key/:tag", routeObjects.createSponsorTagRoute, JsonUtil.json());
                    post("delete/:event-key/:tagId", routeObjects.deleteSponsorTagRoute, JsonUtil.json());
                });

                path("sponsor/", () -> {
                    post("create/:event-key", routeObjects.createSponsorRoute, JsonUtil.json());
                    post("delete/:event-key/:sponsorId", routeObjects.deleteSponsorRoute, JsonUtil.json());
                });

                path("floor/", () -> {
                    post("create/:event-key", routeObjects.createFloorRoute, JsonUtil.json());
                    post("delete/:event-key/:floorId", routeObjects.deleteFloorRoute, JsonUtil.json());
                });

                path("room/", () -> {
                    post("create/:event-key", routeObjects.createRoomRoute, JsonUtil.json());
                    post("delete/:event-key/:roomId", routeObjects.deleteRoomRoute, JsonUtil.json());
                });

            });

            path("image/", () -> {
                post("upload", routeObjects.imageUploadRoute, JsonUtil.json());
                get("get/:id", routeObjects.imageGetRoute, JsonUtil.json());
            });

        });

        after((req, res) -> res.type("application/json"));


    }
}
