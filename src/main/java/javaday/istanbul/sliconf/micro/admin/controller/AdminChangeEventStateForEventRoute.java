package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.model.BaseEventState;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.event.service.EventStateService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.util.UserHelper;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;


@Api
@Path("/service/admin/change/event-state/:eventId/:stateId")
@Produces("application/json")
@Component
@AllArgsConstructor
public class AdminChangeEventStateForEventRoute implements Route {

    private final EventStateService eventStateService;
    private final EventRepositoryService eventRepositoryService;
    private final UserHelper userHelper;

    @POST
    @ApiOperation(value = "Change event state", nickname = "AdminChangeEventStateForEventRoute")
    @ApiImplicitParams({})
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {

        String eventId = request.params("eventId");
        String stateId = request.params("stateId");

        return changeEventState(eventId, stateId);
    }

    public ResponseMessage changeEventState(String eventId, String stateId) {


        ResponseMessage responseMessage = userHelper.checkUserRoleIs(Constants.ROLE_ADMIN);

        if(!responseMessage.isStatus())
            return responseMessage;

        if (Objects.isNull(eventId)) {
            return new ResponseMessage(false, "Event Id can not be null!", "");
        }


        if (Objects.isNull(stateId)) {
            return new ResponseMessage(false, "State Id can not be null!", "");
        }

        Event event = eventRepositoryService.findOne(eventId);

        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not found with given id", "");
        }

        BaseEventState eventState = eventStateService.findOne(stateId);

        if (Objects.isNull(eventState)) {
            return new ResponseMessage(false, "Event state can not found with given id", "");
        }

        event.setEventState(eventState);

        responseMessage = eventRepositoryService.saveAdmin(event);

        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        return new ResponseMessage(true, "Event state successfully changed", event);
    }
}
