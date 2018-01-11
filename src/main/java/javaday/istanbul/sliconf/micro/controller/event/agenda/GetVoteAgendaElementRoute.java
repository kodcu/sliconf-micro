package javaday.istanbul.sliconf.micro.controller.event.agenda;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.agenda.Star;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.star.StarRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;


@Api
@Path("/service/events/agenda/get-vote/:eventId/:sessionId/:userId")
@Produces("application/json")
@Component
public class GetVoteAgendaElementRoute implements Route {

    private StarRepositoryService starRepositoryService;

    private Logger logger = LoggerFactory.getLogger(GetVoteAgendaElementRoute.class);

    @Autowired
    public GetVoteAgendaElementRoute(StarRepositoryService starRepositoryService) {
        this.starRepositoryService = starRepositoryService;
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
        ResponseMessage responseMessage;

        String eventId = request.params("eventId");
        String sessionId = request.params("sessionId");
        String userId = request.params("userId");

        if (Objects.isNull(eventId) || eventId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "EventId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(sessionId) || sessionId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "SessionId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(userId) || userId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "userId can not be empty!", new Object());
            return responseMessage;
        }

        return getVote(eventId, sessionId, userId);
    }

    private ResponseMessage getVote(String eventId, String sessionId, String userId) {
        Star star = starRepositoryService.getStarByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);

        if (Objects.isNull(star)) {
            return new ResponseMessage(false, "Vote can not found!", new Object());
        }

        return new ResponseMessage(true, "Vote fetched!", star);
    }
}
