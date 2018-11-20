package javaday.istanbul.sliconf.micro.user.schedule.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import javaday.istanbul.sliconf.micro.user.schedule.UserScheduleRepositoryService;
import javaday.istanbul.sliconf.micro.user.schedule.UserScheduleService;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/schedule/remove")
@Produces("application/json")
@Component
public class RemoveFromScheduleRoute implements Route {

    private UserScheduleService userScheduleRepositoryService;

    @Autowired
    public RemoveFromScheduleRoute(UserScheduleRepositoryService userScheduleRepositoryService) {
        this.userScheduleRepositoryService = userScheduleRepositoryService;
    }

    @DELETE
    @ApiOperation(value = "Removes an agenda element from users schedule", nickname = "RemoveFromScheduleRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataTypeClass = UserScheduleElement.class, paramType = "body"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            return new ResponseMessage(false, "Body can not be empty!", new Object());
        }

        UserScheduleElement userScheduleElement = JsonUtil.fromJson(body, UserScheduleElement.class);

        return removeFromSchedule(userScheduleElement);
    }

    public ResponseMessage removeFromSchedule(UserScheduleElement userScheduleElement) {
        if (Objects.isNull(userScheduleElement)) {
            return new ResponseMessage(false, "User schedule Element can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getEventId()) || userScheduleElement.getEventId().isEmpty()) {
            return new ResponseMessage(false, "Event Id can not be empty", userScheduleElement);
        }

        if (Objects.isNull(userScheduleElement.getSessionId()) || userScheduleElement.getSessionId().isEmpty()) {
            return new ResponseMessage(false, "Session Id can not be empty", userScheduleElement);
        }

        if (Objects.isNull(userScheduleElement.getEventId()) || userScheduleElement.getEventId().isEmpty()) {
            return new ResponseMessage(false, "Event Id can not be empty", userScheduleElement);
        }

        if (Objects.isNull(userScheduleElement.getUserId()) || userScheduleElement.getUserId().isEmpty()) {
            return new ResponseMessage(false, "User Id can not be empty", userScheduleElement);
        }

        if (Objects.isNull(userScheduleElement.getId()) || userScheduleElement.getId().isEmpty()) {
            return new ResponseMessage(false, "Id can not be empty", userScheduleElement);
        }

        List<UserScheduleElement> userScheduleElementsBefore = new ArrayList<>(userScheduleRepositoryService
                .findByUserIdAndEventId(userScheduleElement.getUserId(), userScheduleElement.getEventId()));

        userScheduleRepositoryService.deleteByIdAndEventIdAndSessionIdAndUserId(
                userScheduleElement.getId(),
                userScheduleElement.getEventId(),
                userScheduleElement.getSessionId(), userScheduleElement.getUserId()
        );


        List<UserScheduleElement> userScheduleElementsAfter = new ArrayList<>(userScheduleRepositoryService
                .findByUserIdAndEventId(userScheduleElement.getUserId(), userScheduleElement.getEventId()));

        if (!isElementDeleted(userScheduleElementsBefore, userScheduleElementsAfter)) {
            return new ResponseMessage(false, "Schedule element can not be removed", userScheduleElement);
        }

        return new ResponseMessage(true, "Schedule element removed", userScheduleElementsAfter);
    }

    private boolean isElementDeleted(List<UserScheduleElement> userScheduleElementsBefore, List<UserScheduleElement> userScheduleElementsAfter) {
        return Objects.nonNull(userScheduleElementsBefore) && Objects.nonNull(userScheduleElementsAfter) &&
                userScheduleElementsBefore.size() > userScheduleElementsAfter.size();
    }
}
