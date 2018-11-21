package javaday.istanbul.sliconf.micro.agenda.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.agenda.AgendaSpecs;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.agenda.model.Star;
import javaday.istanbul.sliconf.micro.comment.StarRepositoryService;
import javaday.istanbul.sliconf.micro.event.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/agenda/vote/:eventId/:sessionId/:userId/:voteValue")
@Produces("application/json")
@Component
@RequiredArgsConstructor
public class VoteAgendaElementRoute implements Route {

    private final UserRepositoryService userRepositoryService;
    private final StarRepositoryService starRepositoryService;
    private final EventRepositoryService eventRepositoryService;
    private final EventControllerMessageProvider ecmp;

    private Logger logger = LoggerFactory.getLogger(VoteAgendaElementRoute.class);


    @POST
    @ApiOperation(value = "Votes a agenda element", nickname = "VoteAgendaElementRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "eventId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "sessionId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "int", name = "voteValue", paramType = "path", allowableValues = "1,2,3,4,5"), //
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
        String stringVote = request.params("voteValue");

        return voteAgenda(eventId, sessionId, userId, stringVote);
    }

    public ResponseMessage voteAgenda(String eventId, String sessionId, String userId, String stringVote) {
        ResponseMessage responseMessage;
        if (Objects.isNull(sessionId) || sessionId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "SessionId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(eventId) || eventId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "EventId can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(stringVote) || stringVote.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "vote can not be empty!", new Object());
            return responseMessage;
        }

        if (Objects.isNull(userId) || userId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "userId can not be empty!", new Object());
            return responseMessage;
        }


        int vote = 0;
        boolean isNumberSafe = true;

        try {
            vote = Integer.parseInt(stringVote);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            isNumberSafe = false;
        }

        if (isNumberSafe) {
            if (vote < 1 || vote > 5) {
                return new ResponseMessage(false, "Vote must be between 1 and 5 ", vote);
            } else {
                return processVote(eventId, sessionId, userId, vote);
            }
        } else {
            return new ResponseMessage(false, "Vote must be integer value", stringVote);
        }
    }


    public synchronized ResponseMessage processVote(String eventId, String sessionId, String userId, int vote) {

        ResponseMessage userResponseMessage = checkIfUserExists(userId);

        if (!userResponseMessage.isStatus()) {
            return userResponseMessage;
        }

        Event event = eventRepositoryService.findOne(eventId);
        userResponseMessage = EventSpecs.checkIfEventStateFinished(event);
        if (userResponseMessage.isStatus()) {
            userResponseMessage.setMessage(ecmp.getMessage("voteFinishedEvent"));
            return userResponseMessage;
        }


        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not found with given id", new Object());
        }

        List<AgendaElement> agendaElements = event.getAgenda();

        AgendaElement agendaElementSource = AgendaSpecs.getAgendaElement(agendaElements, sessionId);

        if (Objects.isNull(agendaElementSource)) {
            return new ResponseMessage(false, "Session can not found with given session id", sessionId);
        }

        List<Star> starList = starRepositoryService.findAllByEventIdAndSessionIdAndUserId(eventId, sessionId, userId);


        ResponseMessage createOrUpdateMessage = createOrUpdateStar(starList, agendaElementSource, eventId, sessionId, userId, vote);

        if (!createOrUpdateMessage.isStatus()) {
            return createOrUpdateMessage;
        }

        updateStarOnAgendaElement(agendaElements, agendaElementSource, sessionId);

        event.setAgenda(agendaElements);

        ResponseMessage eventResponseMessage = eventRepositoryService.save(event);

        if (!eventResponseMessage.isStatus()) {
            return eventResponseMessage;
        }

        //* user kontrol et
        //* event kontrol et
        //* session kontrol et
        //* daha once oy vermis mi kontrol et
        //* verdi ise replace et
        //* vermedi ise yeni oyu olustur
        //* agendanın starını güncelle

        return new ResponseMessage(true, "Session voted", agendaElementSource);
    }

    private ResponseMessage checkIfUserExists(String userId) {
        User user = userRepositoryService.findById(userId).orElse(null);

        if (Objects.nonNull(user)) {
            return new ResponseMessage(true, "User found with given id", user);
        } else {
            return new ResponseMessage(false, "User can not found with given id", userId);
        }
    }

    private void updateStarOnAgendaElement(List<AgendaElement> agendaElements, AgendaElement agendaElementSource, String sessionId) {
        for (AgendaElement element : agendaElements) {
            if (Objects.nonNull(element) && Objects.nonNull(element.getId()) &&
                    element.getId().equals(sessionId)) {
                element.setVoteCount(agendaElementSource.getVoteCount());

                if (!Double.isNaN(agendaElementSource.getStar())) {
                    element.setStar(agendaElementSource.getStar());
                }
            }
        }
    }

    private ResponseMessage createOrUpdateStar(List<Star> starList, AgendaElement agendaElementSource,
                                               String eventId, String sessionId, String userId, int vote) {
        if (Objects.nonNull(starList) && !starList.isEmpty() && Objects.nonNull(starList.get(0))) {
            // oyu guncelle
            Star star = starList.get(0);

            int oldVote = star.getValue();
            star.setValue(vote);
            ResponseMessage starResponseMessage = starRepositoryService.saveStar(star);

            if (!starResponseMessage.isStatus()) {
                return starResponseMessage;
            }

            saveOnAgendaElementStar(agendaElementSource, vote, oldVote);

        } else {
            // yeni oy olustur
            Star newStar = new Star();

            newStar.setValue(vote);
            newStar.setEventId(eventId);
            newStar.setSessionId(sessionId);
            newStar.setUserId(userId);

            ResponseMessage starResponseMessage = starRepositoryService.saveStar(newStar);

            if (!starResponseMessage.isStatus()) {
                return starResponseMessage;
            }

            long agendaVoteCount = agendaElementSource.getVoteCount();
            double agendaStarValue = !Double.isNaN(agendaElementSource.getStar()) ? agendaElementSource.getStar() : 0.0;

            double newVoteMean;

            if (agendaVoteCount != 0) {
                newVoteMean = ((agendaVoteCount * agendaStarValue) + vote) / (agendaVoteCount + 1);
            } else {
                newVoteMean = vote;
            }

            agendaElementSource.setVoteCount(agendaVoteCount + 1);
            agendaElementSource.setStar(newVoteMean);
        }

        return new ResponseMessage(true, "Star updated", agendaElementSource);
    }

    private void saveOnAgendaElementStar(AgendaElement agendaElementSource, int vote, int oldVote) {
        long agendaVoteCount = agendaElementSource.getVoteCount();
        double agendaStarValue = !Double.isNaN(agendaElementSource.getStar()) ? agendaElementSource.getStar() : 0.0;

        double newVoteMean;

        if (agendaVoteCount != 0) {
            newVoteMean = ((agendaVoteCount * agendaStarValue) - oldVote + vote) / agendaVoteCount;
        } else {
            newVoteMean = vote;
        }

        agendaElementSource.setStar(newVoteMean);
    }
}
