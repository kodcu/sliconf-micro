package javaday.istanbul.sliconf.micro.agenda.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.agenda.AgendaSpecs;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.event.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/agenda/create/:event-key")
@Produces("application/json")
@Component
public class CreateAgendaRoute implements Route {


    private EventRepositoryService repositoryService;

    private EventControllerMessageProvider eventControllerMessageProvider;

    @Autowired
    public CreateAgendaRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates a agenda", nickname = "CreateAgendaRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = AgendaElement[].class, name = "speakers", paramType = "body"), //
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

        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Body can not be empty!", new Object());
            return responseMessage;
        }

        final List<AgendaElement> agenda = JsonUtil.fromJsonForList(body, AgendaElement.class);

        String eventKey = request.params("event-key");

        return saveAgenda(agenda, eventKey);
    }

    /**
     * Gelen agenda yi eventkey ile gerekli evente  ekler
     *
     * @param agenda
     * @param eventKey
     * @return
     */
    public ResponseMessage saveAgenda(List<AgendaElement> agenda, String eventKey) {

        ResponseMessage responseMessage;

        if (Objects.isNull(eventKey) || eventKey.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Event key can not empty", new Object());
            return responseMessage;
        }

        Event event = repositoryService.findEventByKeyEquals(eventKey);

        responseMessage = EventSpecs.checkIfEventStateFinished(event);
        if (responseMessage.isStatus()) {
            responseMessage.setMessage(eventControllerMessageProvider.getMessage("updateFinishedEvent"));
            return responseMessage;
        }


        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    "Event can not found by given key", new Object());
            return responseMessage;
        }

        ResponseMessage responseMessageValid = AgendaSpecs.isAgendaValid(agenda);

        if (!responseMessageValid.isStatus()) {
            return responseMessageValid;
        }

        agenda = AgendaSpecs.sortAgenda(agenda);

        event.setAgenda(agenda);

        generateSpeakerTopics(event);

        // Eger agenda elemani yok ise event status u false olur ve mobilde goruntulenmez
        if (Objects.nonNull(agenda) && !agenda.isEmpty()) {
            event.setStatus(true);
        } else {
            event.setStatus(false);
        }

        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Agenda saved successfully", agenda);

        return responseMessage;
    }

    private void generateSpeakerTopics(Event event) {
        if (Objects.nonNull(event) && Objects.nonNull(event.getAgenda())) {

            List<Speaker> speakers = event.getSpeakers();

            removeOldTopics(speakers);

            for (AgendaElement agendaElement : event.getAgenda()) {
                findSpeakerAndSetTopics(agendaElement, speakers);
            }

            event.setSpeakers(speakers);
        }
    }

    private void removeOldTopics(List<Speaker> speakers) {
        if (Objects.nonNull(speakers)) {
            speakers.forEach(speaker -> {
                if (Objects.nonNull(speaker)) {
                    speaker.setTopics(new ArrayList<>());
                }
            });
        }
    }

    private void findSpeakerAndSetTopics(AgendaElement agendaElement, List<Speaker> speakers) {
        for (Speaker speaker : speakers) {
            if (Objects.nonNull(speaker) && Objects.nonNull(agendaElement) &&
                    agendaElement.getLevel() != Constants.Agenda.BREAK &&
                    Objects.nonNull(speaker.getId()) &&
                    speaker.getId().equals(agendaElement.getSpeaker())) {
                if (Objects.isNull(speaker.getTopics())) {
                    speaker.setTopics(new ArrayList<>());
                }
                speaker.getTopics().add(agendaElement.getTopic());
            }
        }
    }
}
