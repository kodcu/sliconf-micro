package javaday.istanbul.sliconf.micro.user.schedule.controller;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.user.model.User;
import javaday.istanbul.sliconf.micro.user.model.UserScheduleElement;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.floor.Floor;
import javaday.istanbul.sliconf.micro.room.Room;
import javaday.istanbul.sliconf.micro.speaker.Speaker;
import javaday.istanbul.sliconf.micro.agenda.model.AgendaElement;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.user.schedule.UserScheduleRepositoryService;
import javaday.istanbul.sliconf.micro.user.service.UserRepositoryService;
import javaday.istanbul.sliconf.micro.agenda.AgendaSpecs;
import javaday.istanbul.sliconf.micro.speaker.SpeakerSpecs;
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
@Path("/service/schedule/add")
@Produces("application/json")
@Component
public class AddToScheduleRoute implements Route {

    private UserScheduleRepositoryService userScheduleRepositoryService;
    private EventRepositoryService eventRepositoryService;
    private UserRepositoryService userRepositoryService;

    @Autowired
    public AddToScheduleRoute(UserScheduleRepositoryService userScheduleRepositoryService,
                              EventRepositoryService eventRepositoryService,
                              UserRepositoryService userRepositoryService) {
        this.userScheduleRepositoryService = userScheduleRepositoryService;
        this.eventRepositoryService = eventRepositoryService;
        this.userRepositoryService = userRepositoryService;
    }

    @POST
    @ApiOperation(value = "Adds an agenda element to users schedule", nickname = "AddToScheduleRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataTypeClass = UserScheduleElement.class, paramType = "body"), //
    }) //
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        String body = request.body();

        if (Objects.isNull(body) || body.isEmpty()) {
            return new ResponseMessage(false, "Body can not be empty!", new Object());
        }

        UserScheduleElement userScheduleElement = JsonUtil.fromJson(body, UserScheduleElement.class);

        return addToSchedule(userScheduleElement);
    }

    public ResponseMessage addToSchedule(UserScheduleElement userScheduleElement) {

        if (Objects.isNull(userScheduleElement)) {
            return new ResponseMessage(false, "User schedule Element can not be empty", new Object());
        }

        ResponseMessage fieldsResponseMessage = isFieldsNotNull(userScheduleElement);

        if (!fieldsResponseMessage.isStatus()) {
            return fieldsResponseMessage;
        }

        ResponseMessage userScheduleElementMessage = prepareUserScheduleElementForSave(userScheduleElement);

        if (!userScheduleElementMessage.isStatus()) {
            return userScheduleElementMessage;
        }

        UserScheduleElement userScheduleElementSaved = userScheduleRepositoryService.save((UserScheduleElement) userScheduleElementMessage.getReturnObject());

        if (Objects.isNull(userScheduleElementSaved)) {
            return new ResponseMessage(false, "Schedule can not saved to db", "");
        }

        List<UserScheduleElement> userScheduleElementList = userScheduleRepositoryService
                .findByUserIdAndEventId(userScheduleElement.getUserId(), userScheduleElement.getEventId());

        return new ResponseMessage(true, "Schedule element added", userScheduleElementList);
    }


    private Room getRoom(List<Room> rooms, String roomId) {
        Room returnRoom = null;

        if (Objects.nonNull(rooms)) {
            for (Room room : rooms) {
                if (Objects.nonNull(room) && Objects.nonNull(room.getId())
                        && room.getId().equals(roomId)) {
                    returnRoom = room;
                    break;
                }
            }
        }

        return returnRoom;
    }

    private Floor getFloor(List<Floor> floors, String floorId) {
        Floor returnFloor = null;

        if (Objects.nonNull(floors)) {
            for (Floor floor : floors) {
                if (Objects.nonNull(floor) && Objects.nonNull(floor.getId())
                        && floor.getId().equals(floorId)) {
                    returnFloor = floor;
                    break;
                }
            }
        }

        return returnFloor;
    }

    private ResponseMessage isFieldsNotNull(UserScheduleElement userScheduleElement) {


        if (Objects.isNull(userScheduleElement.getEventId()) || userScheduleElement.getEventId().isEmpty()) {
            return new ResponseMessage(false, "Event Id can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getSessionId()) || userScheduleElement.getSessionId().isEmpty()) {
            return new ResponseMessage(false, "Session Id can not be empty", new Object());
        }

        if (Objects.isNull(userScheduleElement.getUserId()) || userScheduleElement.getUserId().isEmpty()) {
            return new ResponseMessage(false, "User Id can not be empty", new Object());
        }

        if (Objects.nonNull(userScheduleElement.getId()) && !userScheduleElement.getId().isEmpty()) {
            return new ResponseMessage(false, "This element already added to schedule", userScheduleElement);
        }

        return new ResponseMessage(true, "", "");
    }

    private ResponseMessage prepareUserScheduleElementForSave(UserScheduleElement userScheduleElement) {
        UserScheduleElement userScheduleElementFromDB = userScheduleRepositoryService.findByUserIdAndEventIdAndSessionId(userScheduleElement.getUserId(), userScheduleElement.getEventId(), userScheduleElement.getSessionId());

        if (Objects.nonNull(userScheduleElementFromDB)) {
            return new ResponseMessage(false, "This element already added to schedule", userScheduleElement);
        }

        User user = userRepositoryService.findById(userScheduleElement.getUserId()).orElse(null);

        if (Objects.isNull(user)) {
            return new ResponseMessage(false, "User can not found with given id", userScheduleElement);
        }

        Event event = eventRepositoryService.findOne(userScheduleElement.getEventId());

        if (Objects.isNull(event)) {
            return new ResponseMessage(false, "Event can not found with given id", userScheduleElement);
        }

        AgendaElement agendaElement = AgendaSpecs.getAgendaElement(event.getAgenda(), userScheduleElement.getSessionId());

        if (Objects.isNull(agendaElement)) {
            return new ResponseMessage(false, "Session can not found with given id", userScheduleElement);
        }

        userScheduleElement.setAgendaElement(agendaElement);

        Speaker speaker = SpeakerSpecs.getSpeaker(event.getSpeakers(), agendaElement.getSpeaker());

        if (Objects.isNull(speaker)) {
            return new ResponseMessage(false, "Speaker can not found with given id", userScheduleElement);
        }

        userScheduleElement.setSpeaker(speaker);

        Room room = getRoom(event.getRooms(), agendaElement.getRoom());

        if (Objects.isNull(room)) {
            return new ResponseMessage(false, "Room can not found with given id", userScheduleElement);
        }

        userScheduleElement.setRoom(room);

        Floor floor = getFloor(event.getFloorPlan(), room.getFloor());

        if (Objects.isNull(floor)) {
            return new ResponseMessage(false, "Floor can not found with given id", userScheduleElement);
        }

        userScheduleElement.setFloor(floor);

        return new ResponseMessage(true, "", userScheduleElement);
    }
}
