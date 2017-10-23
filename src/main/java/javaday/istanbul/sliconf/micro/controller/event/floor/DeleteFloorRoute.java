package javaday.istanbul.sliconf.micro.controller.event.floor;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
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
import java.util.stream.Collectors;


@Api
@Path("/service/events/floor/delete/:event-key/:floorId")
@Produces("application/json")
@Component
public class DeleteFloorRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public DeleteFloorRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Deletes a floor", nickname = "DeleteFloorRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "floorId", paramType = "path"), //
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

        String floorId = request.params("floorId");

        if (Objects.isNull(floorId) || floorId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Floor Id can not empty", new Object());
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

        if (Objects.isNull(event.getFloorPlan())) {
            event.setFloorPlan(new ArrayList<>());
        }

        event.setFloorPlan(
                event.getFloorPlan().stream()
                        .filter(floor -> !floorId.equals(floor.getId()))
                        .collect(Collectors.toList())
        );

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Floor deleted successfully", "");

        return responseMessage;
    }
}
