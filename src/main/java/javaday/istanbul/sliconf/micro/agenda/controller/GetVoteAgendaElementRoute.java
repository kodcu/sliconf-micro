package javaday.istanbul.sliconf.micro.agenda.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.agenda.model.Star;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.comment.StarRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Api
@Path("/service/events/agenda/get-vote/:eventId/:sessionId/:userId")
@Produces("application/json")
@Component
public class GetVoteAgendaElementRoute implements Route {

    private StarRepositoryService starRepositoryService;
    private EventRepositoryService eventRepositoryService;

    @Autowired
    public GetVoteAgendaElementRoute(StarRepositoryService starRepositoryService,
                                     EventRepositoryService eventRepositoryService) {
        this.starRepositoryService = starRepositoryService;
        this.eventRepositoryService = eventRepositoryService;
    }

    @GET
    @ApiOperation(value = "Gets votes from agenda element for user", nickname = "GetVoteAgendaElementRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "sessionId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path") //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String eventId = request.params("eventId");
        String sessionId = request.params("sessionId");
        String userId = request.params("userId");

        return getVote(eventId, sessionId, userId);
    }

    public ResponseMessage getVote(String eventId, String sessionId, String userId) {

        if (Objects.isNull(eventId) || eventId.isEmpty()) {
            return new ResponseMessage(false,
                    "EventId can not be empty!", new Object());
        }

        if (Objects.isNull(sessionId) || sessionId.isEmpty()) {
            return new ResponseMessage(false,
                    "SessionId can not be empty!", new Object());
        }

        if (Objects.isNull(userId) || userId.isEmpty()) {
            return new ResponseMessage(false,
                    "userId can not be empty!", new Object());
        }

        Star star = starRepositoryService.getStarByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);

        if (Objects.isNull(star)) {
            return new ResponseMessage(false, "Vote can not found!", new Object());
        }

        Map<String, Object> returnMap = new HashMap<>();

        returnMap.put("vote", star);


        Event event = eventRepositoryService.findOne(eventId);

        if (Objects.nonNull(event) && Objects.nonNull(event.getAgenda())) {
            List<AgendaElement> agendaElementList = event.getAgenda().stream().filter(element ->
                    Objects.nonNull(element) && Objects.nonNull(element.getId()) &&
                            element.getId().equals(sessionId))
                    .collect(Collectors.toList());

            if (Objects.nonNull(agendaElementList) && Objects.nonNull(agendaElementList.get(0))) {
                returnMap.put("session", agendaElementList.get(0));
            }

        }

        return new ResponseMessage(true, "Vote fetched!", returnMap);
    }
}
