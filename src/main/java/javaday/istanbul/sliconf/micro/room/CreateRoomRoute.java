package javaday.istanbul.sliconf.micro.room;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.event.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.event.EventSpecs;
import javaday.istanbul.sliconf.micro.event.model.Event;
import javaday.istanbul.sliconf.micro.event.service.EventRepositoryService;
import javaday.istanbul.sliconf.micro.response.ResponseMessage;
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
import java.util.UUID;


@Api
@Path("/service/events/room/create/:event-key")
@Produces("application/json")
@Component
public class CreateRoomRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public CreateRoomRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates a room", nickname = "CreateRoomRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = Room[].class, name = "room", paramType = "body"), //
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
                    "Body can not empty!", new Object());
            return responseMessage;
        }

        List<Room> roomList = JsonUtil.fromJsonForList(body, Room.class);

        String eventKey = request.params("event-key");

        return saveRooms(roomList, eventKey);
    }

    public ResponseMessage saveRooms(List<Room> roomList, String eventKey) {
        ResponseMessage responseMessage;

        if (Objects.isNull(eventKey) || eventKey.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Event key can not empty", new Object());
            return responseMessage;
        }

        Event event = repositoryService.findEventByKeyEquals(eventKey);

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    "Event can not found by given key", new Object());
            return responseMessage;
        }

        EventControllerMessageProvider ecmp = new EventControllerMessageProvider();
        responseMessage = EventSpecs.checkIfEventStateFinished(event);
        if (responseMessage.isStatus()) {
            responseMessage.setMessage(ecmp.getMessage("updateFinishedEvent"));
            return responseMessage;
        }


        generateRoomId(roomList);

        event.setRooms(roomList);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Rooms saved successfully", roomList);

        return responseMessage;
    }

    private void generateRoomId(List<Room> roomList) {
        if (Objects.nonNull(roomList)) {
            roomList.forEach(room -> {
                if (Objects.nonNull(room) &&
                        (Objects.isNull(room.getId()) || room.getId().contains("newid"))) {
                    room.setId(UUID.randomUUID().toString());
                }
            });
        }
    }
}
