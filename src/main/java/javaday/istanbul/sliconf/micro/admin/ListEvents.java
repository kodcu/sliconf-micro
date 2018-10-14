package javaday.istanbul.sliconf.micro.admin;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.valueOf;

@AllArgsConstructor
@Api(value = "admin", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/admin/events")
@Produces("application/json")
@Component
public class ListEvents implements Route {

    private final AdminService adminService;

    @GET
    @ApiOperation(value = "Lists events for admin", nickname = "AdminListEventsRoute")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //

            @ApiImplicitParam(
                    name = "lifeCycleStates", paramType = "query",
                    defaultValue = "UPCOMING, ACTIVE",
                    dataType = "string",
                    allowableValues = "ACTIVE, PASSIVE, UPCOMING, HAPPENING, FINISHED, DELETED, FAILED",
                    example = "/events?lifeCycleStates=PASSIVE&lifeCycleStates=UPCOMING --> List upcoming passive events"
                    ),

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request,
                                  @ApiParam(hidden = true) Response response) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority(Constants.ROLE_ADMIN))) {
            return new ResponseMessage(false, "You have no authorization to do this!", new Object());
        }

        String[] lifeCycleStatesQuery = request.queryParamsValues("lifeCycleStates");

        String pageSize = request.queryParamOrDefault("pageSize", "20");
        String pageNumber = request.queryParamOrDefault("pageNumber", "0");
        Pageable pageable;

        try {
            pageable = new PageRequest(Integer.parseInt(pageNumber), Integer.parseInt(pageSize));
        } catch (NumberFormatException e) {
            pageable = new PageRequest(0, 20);
        }

        Page<Event> events = adminService.filter(lifeCycleStatesQuery, pageable);

        return new ResponseMessage(true, "Events listed. Total Events = "+ valueOf(events.getTotalElements()), events.getContent());

    }
}