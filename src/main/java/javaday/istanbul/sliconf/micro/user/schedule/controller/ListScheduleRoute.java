package javaday.istanbul.sliconf.micro.user.schedule.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.schedule.UserScheduleRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/schedule/list/:userId/:eventId")
@Produces("application/json")
@Component
public class ListScheduleRoute implements Route {

    private UserScheduleRepositoryService userScheduleRepositoryService;
    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;

    private final List emptyArray = new ArrayList();

    @Autowired
    public ListScheduleRoute(UserScheduleRepositoryService userScheduleRepositoryService,
                             EventRepositoryService eventRepositoryService,
                             UserRepositoryService userRepositoryService) {
        this.userScheduleRepositoryService = userScheduleRepositoryService;
        this.eventRepositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @GET
    @ApiOperation(value = "Lists users schedule", nickname = "ListScheduleRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataTypeClass = UserScheduleElement.class, paramType = "body"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId", paramType = "path"), //

    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String userId = request.params("userId");
        String eventId = request.params("eventId");

        return listSchedule(userId, eventId);
    }

    public ResponseMessage listSchedule(String userId, String eventId) {

        if (Objects.isNull(eventId) || eventId.isEmpty()) {
            return new ResponseMessage(false, "Event Id can not be empty", emptyArray);
        }

        if (Objects.isNull(userId) || userId.isEmpty()) {
            return new ResponseMessage(false, "User Id can not be empty", emptyArray);
        }

        User user = userRepositoryService.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            return new ResponseMessage(false, "User can not found with given id", emptyArray);
        }

        Event event = eventRepositoryService.findOne(eventId);

        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not found with given id", emptyArray);
        }

        List<UserScheduleElement> userScheduleElements = userScheduleRepositoryService.findByUserIdAndEventId(userId, eventId);

        if (Objects.isNull(userScheduleElements)) {
            return new ResponseMessage(false, "Schedule list is empty", emptyArray);
        }

        return new ResponseMessage(true, "Schedule list", userScheduleElements);
    }
}
