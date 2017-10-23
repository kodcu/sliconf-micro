package javaday.istanbul.sliconf.micro.controller.event.room;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Room;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;


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
            @ApiImplicitParam(required = true, dataTypeClass = Room.class, name = "room", paramType = "body"), //
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

        Room room = JsonUtil.fromJson(body, Room.class);

        if (Objects.isNull(room) ||
                Objects.isNull(room.getFloor()) || room.getFloor().isEmpty() ||
                Objects.isNull(room.getLabel()) || room.getLabel().isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Room data must be filled, not empty!", new Object());
            return responseMessage;
        }

        String eventKey = request.params("event-key");

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

        if (Objects.isNull(event.getRooms())) {
            event.setRooms(new ArrayList<>());
        }

        int tagId = 1;

        while (event.getRooms()
                .stream()
                .anyMatch(isIdInList("ri" + tagId))) {
            tagId++;
        }

        room.setId("ri" + tagId);

        event.getRooms().add(room);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Room saved successfully", room.getId());

        return responseMessage;
    }

    private Predicate<Room> isIdInList(String id) {
        return p -> id.equals(p.getId());
    }
}
