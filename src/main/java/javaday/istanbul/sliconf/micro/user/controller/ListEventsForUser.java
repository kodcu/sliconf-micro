package javaday.istanbul.sliconf.micro.user.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.model.EventFilter;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
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

@Api(value = "user", authorizations = {@Authorization(value = "Bearer")})
@Path("/service/users/events")
@Produces("application/json")
@AllArgsConstructor
@Component
public class ListEventsForUser implements Route {

    private final UserRepositoryService userRepositoryService;

    @GET
    @ApiOperation(value = "Lists events for user", nickname = "ListEventsForUser")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header",
                    example = "Authorization: Bearer <tokenValue>"), //

            @ApiImplicitParam(
                    name = "lifeCycleStates", paramType = "query",
                    defaultValue = "ACTIVE, HAPPENING",
                    dataType = "string",
                    allowableValues = "ACTIVE, PASSIVE, HAPPENING, FINISHED, DELETED, FAILED",
                    example = "/events?lifeCycleStates=PASSIVE,ACTIVE --> List active or passive events",
                    value = "We use this parameter to list events by life cycle status. An event can have different status.\n" +
                            "\n" +
                            "ACTIVE = It means that the event is created and the necessary information is filled out and" +
                            " visible from the mobile.\n" +
                            "\n" +
                            "PASSIVE =  It means that the event was created but the required information has not yet been entered.\n" +
                            "\n" +
                            "\n" +
                            "HAPPENING = Indicates that the event is currently happening. If the event starts in the ACTIVE " +
                            "state, the status of the event will be HAPPENING.\n" +
                            "\n" +
                            "FINISHED = It means that the event is finished successfully. This is the case if the event " +
                            "ends when have the status HAPPENING.\n" +
                            "\n" +
                            "FAILED = If the event starts and the required information is not yet entered, the status " +
                            "is received. In other words, if the event starts in PASSIVE status, the status will be FAILED.\n" +
                            "\n" +
                            "DELETED = Means the event has been deleted by the event manager."
            ),

            @ApiImplicitParam(dataType = "string", name = "name",
                    paramType = "query",
                    example = "/events?name=Java&lifeCycleStates=PASSIVE,ACTIVE " +
                            "--> Lists active or passive events that event name contains 'Java'"),

            @ApiImplicitParam(dataType = "string", name = "pageSize",
                    paramType = "query", defaultValue = "20"),
            @ApiImplicitParam(dataType = "string", name = "pageNumber",
                    paramType = "query", defaultValue = "0")


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

        EventFilter eventFilter = EventSpecs.getEventFilterFromRequest(request);
        Pageable pageable = EventSpecs.getPageableFromRequest(request);

        Page<Event> events = userRepositoryService.filter(eventFilter, pageable);
        String message = "Events listed. Total Events = " + events.getTotalElements();
        return new ResponseMessage(true, message, events.getContent());

    }
}
