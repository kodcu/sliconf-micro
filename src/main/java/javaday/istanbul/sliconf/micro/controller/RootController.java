package javaday.istanbul.sliconf.micro.controller;

import javaday.istanbul.sliconf.micro.survey.SurveyException;
import javaday.istanbul.sliconf.micro.model.response.ResponseError;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.SwaggerParser;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger logger = LoggerFactory.getLogger(RootController.class);

    @Autowired
    public RootController(RouteObjects routeObjects) {// NOSONAR
        RootController.routeObjects = routeObjects;// NOSONAR
    }

    public static void setPaths() {

        try {
            // Build swagger json description
            final String swaggerJson = SwaggerParser.getSwaggerJson(APP_PACKAGE);
            get("/swagger", (req, res) -> swaggerJson);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(HttpStatus.BAD_REQUEST.value());
            res.body(JsonUtil.toJson(new ResponseError(e)));
        });

        // Using Route
        notFound((req, res) -> {
            res.type("application/json");

            res.status(200);

            ResponseMessage responseMessage = new ResponseMessage();
            responseMessage.setMessage("The page you looking for not found!");
            responseMessage.setReturnObject(req.url());
            responseMessage.setStatus(false);

            return JsonUtil.toJson(responseMessage);
        });

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

        path("/service/", () -> {
            path("users/", () -> {
                post("login", routeObjects.loginUserRoute, JsonUtil.json());
                post("login/anonymous/:deviceId", routeObjects.loginUserAnonymousRoute, JsonUtil.json());
                post("login/auth/:serviceName/:token", routeObjects.loginUserAuthRoute, JsonUtil.json());

                post("register", routeObjects.createUserRoute, JsonUtil.json());
                post("register/anonymous/:deviceId", routeObjects.createUserAnonymousRoute, JsonUtil.json());

                post("update", routeObjects.updateUserRoute, JsonUtil.json());
                post("password-reset/send/:email", routeObjects.sendPasswordResetRoute, JsonUtil.json());
                post("password-reset/reset/:token", routeObjects.resetPasswordRoute, JsonUtil.json());
            });

            path("events/", () -> {

                post("create/:userId", routeObjects.createEventRoute, JsonUtil.json());
                delete("delete/:eventId/:userId", routeObjects.deleteEventRoute, JsonUtil.json());

                get("list/:userId", routeObjects.listEventsRoute, JsonUtil.json());

                path("get/", () -> {
                    get("with-key/:key", routeObjects.getEventWithKeyRoute, JsonUtil.json());
                    get("statistics/:key", routeObjects.getStatisticsRoute, JsonUtil.json());
                });

                before();

                final String createEventKey = "create/:event-key";

                path("sponsor/", () ->
                        post(createEventKey, routeObjects.createSponsorRoute, JsonUtil.json())
                );

                path("floor/", () ->
                        post(createEventKey, routeObjects.createFloorRoute, JsonUtil.json())
                );

                path("room/", () ->
                        post(createEventKey, routeObjects.createRoomRoute, JsonUtil.json())
                );

                path("speaker/", () ->
                        post(createEventKey, routeObjects.createSpeakerRoute, JsonUtil.json())
                );

                path("agenda/", () -> {
                    post(createEventKey, routeObjects.createAgendaRoute, JsonUtil.json());
                    post("vote/:eventId/:sessionId/:userId/:voteValue", routeObjects.voteAgendaElementRoute, JsonUtil.json());
                    get("get-vote/:eventId/:sessionId/:userId", routeObjects.getVoteAgendaElementRoute, JsonUtil.json());
                });

                path("comment/", () -> {
                    post("add-new", routeObjects.addNewCommentRoute, JsonUtil.json());
                    post("moderate", routeObjects.moderateCommentRoute, JsonUtil.json());

                    post("vote/:commentId/:userId/:voteValue", routeObjects.voteCommentRoute, JsonUtil.json());

                    get("list/:status/:eventId", routeObjects.listCommentsRoute, JsonUtil.json());
                    get("list/:status/:eventId/:sessionId", routeObjects.listCommentsRoute, JsonUtil.json());
                    get("list/:status/:eventId/:sessionId/:userId", routeObjects.listCommentsRoute, JsonUtil.json());

                });

                path(":eventIdentifier/surveys", () -> {
                    post("", routeObjects.createNewSurvey, JsonUtil.json());
                    put("", routeObjects.updateSurveyRoute, JsonUtil.json());
                    get("", routeObjects.getSurveys, JsonUtil.json());
                    delete("/:surveyId", routeObjects.removeSurvey, JsonUtil.json());
                    get("/:surveyId", routeObjects.getSurvey, JsonUtil.json());
                    post("/:surveyId/view", routeObjects.userViewedSurvey, JsonUtil.json());

                    path("/:surveyId/answers", () -> {
                        post("", routeObjects.submitAnswers, JsonUtil.json());
                        get("", routeObjects.getAnswers, JsonUtil.json());
                        put("/:answerId", routeObjects.updateAnswers, JsonUtil.json());

                    });

                    exception(SurveyException.class, (exception, request1, response1)-> {
                        String message = exception.getMessage();
                        Object rejectedValue = exception.getRejectedValue();
                        ResponseMessage responseMessage = new ResponseMessage(false, message, rejectedValue);
                        response1.type("application/json");
                        response1.body(JsonUtil.toJson(responseMessage));

                    });
                });

                path(":eventKey/statistics", () -> {
                    get("/sessions", routeObjects.getEventSessionsStatistics, JsonUtil.json());
                });
            });

            path("schedule/", () -> {
                post("add", routeObjects.addToScheduleRoute, JsonUtil.json());
                delete("remove", routeObjects.removeFromScheduleRoute, JsonUtil.json());
                get("list/:userId/:eventId", routeObjects.listScheduleRoute, JsonUtil.json());
            });

            path("admin/", () -> {
                path("list/", () -> {
                    get("users", routeObjects.adminListUsersRoute, JsonUtil.json());
                    get("events", routeObjects.adminListEventsRoute, JsonUtil.json());
                    get("event-states", routeObjects.adminListEventStatesRoute, JsonUtil.json());
                });

                path("change/", () ->
                    post("event-state/:eventId/:stateId", routeObjects.adminChangeEventStateForEventRoute, JsonUtil.json())
                );

                path("users/", () ->
                        get(":userId", routeObjects.adminGetUserInfo, JsonUtil.json())
                );
            });

            path("image/", () -> {
                post("upload", routeObjects.imageUploadRoute, JsonUtil.json());
                get("get/:id", routeObjects.imageGetRoute, JsonUtil.json());
            });

        });

        after((req, res) -> {

            if (!"image/png".equals(res.type())) {
                res.type("application/json");
            }
        });


    }
}
