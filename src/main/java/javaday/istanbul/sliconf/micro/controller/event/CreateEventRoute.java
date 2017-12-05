package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
@Path("/service/events/create/:userId")
@Produces("application/json")
@Component
public class CreateEventRoute implements Route {


    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService repositoryService;

    private UserRepositoryService userRepositoryService;

    @Autowired
    public CreateEventRoute(EventControllerMessageProvider messageProvider,
                            EventRepositoryService eventRepositoryService,
                            UserRepositoryService userRepositoryService) {
        this.messageProvider = messageProvider;
        this.repositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates an event and bind with given userId", nickname = "CreateEventRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "userId", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = Event.class, paramType = "body") //
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

        String userId = request.params("userId");

        if (Objects.isNull(userId) || userId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventUserIdCantBeEmpty"), new Object());
            return responseMessage;
        }

        if (Objects.isNull(body) || body.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventBodyCantBeEmpty"), new Object());
            return responseMessage;
        }

        Event event = JsonUtil.fromJson(body, Event.class);

        if (Objects.isNull(event.getKey()) || event.getKey().isEmpty()) {
            responseMessage = saveNewEvent(event, userId);
        } else {
            responseMessage = updateEvent(event, userId);
        }

        return responseMessage;
    }

    private ResponseMessage saveNewEvent(Event event, String userId) {
        ResponseMessage responseMessage;

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if (!EventSpecs.checkEventName(event, 4)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), new Object());
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if (!EventSpecs.checkIfEventDateAfterOrInNow(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), new Object());
            return responseMessage;
        }

        // event var mı diye kontrol et
        List<Event> dbEvents = repositoryService.findByName(event.getName());

        if (Objects.nonNull(dbEvents) && !dbEvents.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyRegistered"), new Object());
            return responseMessage;
        }

        //Kanban numarası oluştur
        EventSpecs.generateKanbanNumber(event);

        User user = userRepositoryService.findById(userId);

        if (Objects.isNull(user)) {
            responseMessage = new ResponseMessage(false,
                    "User can not found with given id", new Object());
            return responseMessage;
        }

        event.setExecutiveUser(userId);


        return saveEvent(event);
    }

    private ResponseMessage updateEvent(Event event, String userId) {
        ResponseMessage responseMessage;

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if (!EventSpecs.checkEventName(event, 4)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), new Object());
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if (!EventSpecs.checkIfEventDateAfterOrInNow(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), new Object());
            return responseMessage;
        }

        // event var mı diye kontrol et
        Event dbEvent = repositoryService.findEventByKeyEquals(event.getKey());

        if (Objects.isNull(dbEvent)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
            return responseMessage;
        }

        copyUpdatedFields(dbEvent, event);

        return saveEvent(dbEvent);
    }

    private ResponseMessage saveEvent(Event event) {
        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        return new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);
    }

    private void copyUpdatedFields(Event dbEvent, Event updatedEvent) {
        dbEvent.setName(updatedEvent.getName());
        dbEvent.setStartDate(updatedEvent.getStartDate());
        dbEvent.setEndDate(updatedEvent.getEndDate());
        dbEvent.setLogoPath(updatedEvent.getLogoPath());
        dbEvent.setDescription(updatedEvent.getDescription());
        dbEvent.setAbout(updatedEvent.getAbout());
        dbEvent.setEmail(updatedEvent.getEmail());
    }
}
