package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.LifeCycleState;
import javaday.istanbul.sliconf.micro.model.User;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.service.event.EventService;
import javaday.istanbul.sliconf.micro.service.user.UserRepositoryService;
import javaday.istanbul.sliconf.micro.specs.EventSpecs;
import javaday.istanbul.sliconf.micro.survey.service.GeneralService;
import javaday.istanbul.sliconf.micro.util.Constants;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Api
@Path("/service/events/create/:userId")
@Produces("application/json")
@Component
@AllArgsConstructor
public class CreateEventRoute implements Route {


    private final EventControllerMessageProvider messageProvider;
    private final EventService repositoryService;

    private final UserRepositoryService userRepositoryService;
    private final GeneralService generalService;


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

        return processEvent(event, userId);
    }

    public ResponseMessage processEvent(Event event, String userId) {

        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not be null", "");
        }

        if (Objects.isNull(event.getKey()) || event.getKey().isEmpty()) {
            return saveNewEvent(event, userId);
        } else {
            return updateEvent(event, userId);
        }
    }

    private ResponseMessage saveNewEvent(Event event, String userId) {
        ResponseMessage responseMessage;

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if (!EventSpecs.checkEventName(event, 4)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), event);
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if (!EventSpecs.checkIfEventDateAfterOrInNow(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), event);
            return responseMessage;
        }

        if (event.getStartDate().plusWeeks(1).isBefore(event.getEndDate())) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventStartAndEndDateInvalid"), event);
            return responseMessage;
        }

        // event var mı diye kontrol et
        List<Event> dbEvents = repositoryService.findByNameAndDeleted(event.getName(), false);

        if (Objects.nonNull(dbEvents) && !dbEvents.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventAlreadyRegistered"), event);
            return responseMessage;
        }

        //Kanban numarası oluştur
        EventSpecs.generateKanbanNumber(event, repositoryService);

        User user = userRepositoryService.findById(userId).orElse(null);

        if (Objects.isNull(user)) {
            responseMessage = new ResponseMessage(false,
                    "User can not found with given id", event);
            return responseMessage;
        }

        LifeCycleState lifeCycleState = new LifeCycleState();
        event.getLifeCycleState().getEventStatuses().add(LifeCycleState.EventStatus.PASSIVE);
        event.getLifeCycleState().getEventStatuses().add(LifeCycleState.EventStatus.UPCOMING);
        event.setLifeCycleState(lifeCycleState);
        event.setExecutiveUser(userId);

        updateUserRoleAndSave(user);

        return saveEvent(event);
    }

    private ResponseMessage updateEvent(Event event, String userId) {
        ResponseMessage responseMessage;

        //isim uzunluğu minimumdan düşük mü diye kontrol et
        if (!EventSpecs.checkEventName(event, 4)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameTooShort"), event);
            return responseMessage;
        }

        //event tarihinin geçip geçmediğin, kontrol et
        if (!EventSpecs.checkIfEventDateAfterOrInNow(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventDataInvalid"), event);
            return responseMessage;
        }

        if (event.getStartDate().plusWeeks(1).isBefore(event.getEndDate())) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventStartAndEntDateInvalid"), event);
            return responseMessage;
        }

        List<Event> dbEvents = repositoryService.findByNameAndNotKeyAndDeleted(event.getName(), event.getKey(), false);

        if (Objects.nonNull(dbEvents) && !dbEvents.isEmpty()) {

            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventNameAlreadyRegistered"), event);
            return responseMessage;
        }

        // event var mı diye kontrol et
        Event dbEvent = repositoryService.findEventByKeyEquals(event.getKey());

        if (Objects.isNull(dbEvent)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), event);
            return responseMessage;
        }

        if (Objects.nonNull(dbEvent.getDeleted()) && dbEvent.getDeleted()) {
            return new ResponseMessage(false, messageProvider.getMessage("canNotUpdateDeletedEvent"), event);
        }

        if (Objects.nonNull(dbEvent.getExecutiveUser()) && !dbEvent.getExecutiveUser().equals(userId)) {
            return new ResponseMessage(false, messageProvider.getMessage("onlyOwnedEventsCanBeUpdated"), event);
        }
        if(LocalDateTime.now().plusHours(48).isAfter(event.getStartDate())) {
                event.setDateLock(true);
        }
        if (!(event.getStartDate().isEqual(dbEvent.getStartDate())) && event.isDateLock())
            return new ResponseMessage(false, messageProvider.getMessage("eventStartDateCanNotBeUpdatedAnymore"), event);
        if (!(event.getStartDate().isEqual(dbEvent.getStartDate())) &&
                event.getStartDate().minusWeeks(1).isBefore(LocalDateTime.now()))
            return new ResponseMessage(false, messageProvider.getMessage("eventStartDateCanNotBeUpdatedGivenDate"), event);


        copyUpdatedFields(dbEvent, event);

        ResponseMessage saveResponse = saveEvent(dbEvent);

        if (!saveResponse.isStatus()) {
            return saveResponse;
        }

        saveResponse.setMessage(messageProvider.getMessage("eventSuccessfullyUpdated"));

        return saveResponse;
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

    /**
     * Eger kullanici bir event olusturmus ise rolu ROLE_USER dan ROLE_EVENT_MANAGER a degisir
     */
    private void updateUserRoleAndSave(User user) {
        if (Objects.nonNull(user) &&
                Constants.DEFAULT_USER_ROLE.equals(user.getRole())) {
            user.setRole(Constants.ROLE_EVENT_MANAGER);

            userRepositoryService.save(user);
        }
    }

    private void copyUpdatedFields(Event dbEvent, Event updatedEvent) {
        dbEvent.setName(updatedEvent.getName());
        dbEvent.setStartDate(updatedEvent.getStartDate());
        dbEvent.setEndDate(updatedEvent.getEndDate());
        dbEvent.setLogoPath(updatedEvent.getLogoPath());
        dbEvent.setDescription(updatedEvent.getDescription());
        dbEvent.setAbout(updatedEvent.getAbout());
    }
}
