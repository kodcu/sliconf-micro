package javaday.istanbul.sliconf.micro.controller.event.floor;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Floor;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
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
import java.util.Objects;
import java.util.function.Predicate;


@Api
@Path("/service/events/floor/create/:event-key")
@Produces("application/json")
@Component
public class CreateFloorRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public CreateFloorRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates a floor", nickname = "CreateFloorRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = Floor.class, name = "floor", paramType = "body"), //
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

        Floor floor = JsonUtil.fromJson(body, Floor.class);

        if (Objects.isNull(floor) ||
                Objects.isNull(floor.getImage()) || floor.getImage().isEmpty() ||
                Objects.isNull(floor.getName()) || floor.getName().isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Floor data must be filled, not empty!", new Object());
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
            event.setFloorPlan(new ArrayList<>());
        }

        int tagId = 1;

        while (event.getFloorPlan()
                .stream()
                .anyMatch(isIdInList("fi" + tagId))) {
            tagId++;
        }

        floor.setId("fi" + tagId);

        event.getFloorPlan().add(floor);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Floor saved successfully", floor.getId());

        return responseMessage;
    }

    private Predicate<Floor> isIdInList(String id) {
        return p -> id.equals(p.getId());
    }
}
