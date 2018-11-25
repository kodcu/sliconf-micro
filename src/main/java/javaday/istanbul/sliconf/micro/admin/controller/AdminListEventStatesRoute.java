package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.admin.AdminService;
import javaday.istanbul.sliconf.micro.event.model.BaseEventState;
import javaday.istanbul.sliconf.micro.event.service.EventStateService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AdminListEventStatesRoute implements Route {

    private final EventStateService eventStateService;
    private final AdminService adminService;

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

        ResponseMessage responseMessage = adminService.checkUserRoleIsAdmin(authentication);

        if(!responseMessage.isStatus())
            return responseMessage;

        List<BaseEventState> eventStates = eventStateService.findAll();

        return new ResponseMessage(true, "Event states listed", eventStates);
    }
}
