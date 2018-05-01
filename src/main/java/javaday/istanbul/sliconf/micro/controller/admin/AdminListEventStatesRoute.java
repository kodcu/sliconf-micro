package javaday.istanbul.sliconf.micro.controller.admin;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.BaseEventState;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/admin/list/event-states")
@Produces("application/json")
@Component
public class AdminListEventStatesRoute implements Route {

    private EventStateService eventStateService;

    @Autowired
    public AdminListEventStatesRoute(EventStateService eventStateService) {
        this.eventStateService = eventStateService;
    }

    @GET
    @ApiOperation(value = "Lists event states for admin", nickname = "AdminListEventStatesRoute")
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

        return getEventStates(authentication);
    }

    public ResponseMessage getEventStates(Authentication authentication) {

        if (Objects.isNull(authentication)) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Constants.ROLE_ADMIN))) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        List<BaseEventState> eventStates = eventStateService.findAll();

        return new ResponseMessage(true, "Event states listed", eventStates);
    }
}
