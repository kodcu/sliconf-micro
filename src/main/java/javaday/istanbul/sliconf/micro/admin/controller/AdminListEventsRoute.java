package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.util.UserHelper;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;


@Api
@Path("/service/admin/list/events")
@Produces("application/json")
@Component
@AllArgsConstructor
public class AdminListEventsRoute implements Route {

    private final EventRepositoryService eventRepositoryService;
    private final UserHelper userHelper;


    @POST
    @ApiOperation(value = "Lists events for admin", nickname = "AdminListEventsRoute")
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
        return getEvents();
    }

    public ResponseMessage getEvents() {

        ResponseMessage responseMessage = userHelper.checkUserRoleIs(Constants.ROLE_ADMIN);

        if(!responseMessage.isStatus())
            return responseMessage;

        List<Event> events = eventRepositoryService.findAll();

        return new ResponseMessage(true, "Events listed", events);
    }
}
