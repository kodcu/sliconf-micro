package javaday.istanbul.sliconf.micro.controller.admin;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.BaseEventState;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventStateService;
import javaday.istanbul.sliconf.micro.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class AdminChangeEventStateForEventRoute implements Route {

    private EventStateService eventStateService;
    private EventRepositoryService eventRepositoryService;

    @Autowired
    public AdminChangeEventStateForEventRoute(EventStateService eventStateService,
                                              EventRepositoryService eventRepositoryService) {
        this.eventStateService = eventStateService;
        this.eventRepositoryService = eventRepositoryService;
    }

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String eventId = request.params("eventId");
        String stateId = request.params("stateId");

        return changeEventState(authentication, eventId, stateId);
    }

    public ResponseMessage changeEventState(Authentication authentication, String eventId, String stateId) {

        if (Objects.isNull(authentication)) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Constants.ROLE_ADMIN))) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

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

        ResponseMessage responseMessage = eventRepositoryService.saveAdmin(event);

        if (!responseMessage.isStatus()) {
            return responseMessage;
        }

        return new ResponseMessage(true, "Event state successfully changed", event);
    }
}
