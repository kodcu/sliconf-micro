package javaday.istanbul.sliconf.micro.admin.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.admin.AdminService;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.EventFilter;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.util.UserHelper;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import static java.lang.String.valueOf;

@AllArgsConstructor
@Api(value = "admin", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/admin/events")
@Produces("application/json")
@Component
public class ListEventsForAdmin implements Route {

    private final UserHelper userHelper;
    private final AdminService adminService;

    @GET
    @ApiOperation(value = "Lists events for admin", nickname = "AdminListEventsRoute")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //

            @ApiImplicitParam(
                    name = "lifeCycleStates", paramType = "query",
                    defaultValue = "ACTIVE",
                    dataType = "string",
                    allowableValues = "ACTIVE, PASSIVE, HAPPENING, FINISHED, DELETED, FAILED",
                    example = "/events?lifeCycleStates=PASSIVE,ACTIVE --> List active or passive events"
            ),

            @ApiImplicitParam(dataType = "string", name = "name",
                    paramType = "query"),

            @ApiImplicitParam(dataType = "string", name = "pageSize",
                    paramType = "query", defaultValue = "20"),
            @ApiImplicitParam(dataType = "string", name = "pageNumber",
                    paramType = "query", defaultValue = "0"),


    })
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })

    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request,
                                  @ApiParam(hidden = true) Response response) throws Exception {

        ResponseMessage responseMessage = userHelper.checkUserRoleIs(Constants.ROLE_ADMIN);

        if(!responseMessage.isStatus())
            return responseMessage;

        EventFilter eventFilter = EventSpecs.getEventFilterFromRequest(request);
        Pageable pageable = EventSpecs.getPageableFromRequest(request);

        Page<Event> events = adminService.filter(eventFilter, pageable);
        String message = "Events listed. Total Events = " + events.getTotalElements();
        return new ResponseMessage(true, message, events.getContent());

    }
}