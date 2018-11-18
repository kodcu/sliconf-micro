package javaday.istanbul.sliconf.micro.floor;

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
            @ApiImplicitParam(required = true, dataTypeClass = Floor[].class, name = "floor", paramType = "body"), //
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

        List<Floor> floorList = JsonUtil.fromJsonForList(body, Floor.class);

        String eventKey = request.params("event-key");

        return saveFloors(floorList, eventKey);
    }

    public ResponseMessage saveFloors(List<Floor> floorList, String eventKey) {
        ResponseMessage responseMessage;

        if (Objects.isNull(eventKey) || eventKey.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Event key can not empty", floorList);
            return responseMessage;
        }

        Event event = repositoryService.findEventByKeyEquals(eventKey);

        EventControllerMessageProvider ecmp = new EventControllerMessageProvider();
        responseMessage = EventSpecs.checkIfEventStateFinished(event);
        if (responseMessage.isStatus()) {
            responseMessage.setMessage(ecmp.getMessage("updateFinishedEvent"));
            return responseMessage;
        }

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    "Event can not found by given key", floorList);
            return responseMessage;
        }

        generateFloorId(floorList);

        event.setFloorPlan(floorList);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            dbResponse.setReturnObject(floorList);
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Floor saved successfully", floorList);

        return responseMessage;
    }

    private void generateFloorId(List<Floor> floorList) {
        if (Objects.nonNull(floorList)) {
            floorList.forEach(floor -> {
                if (Objects.nonNull(floor) &&
                        (Objects.isNull(floor.getId()) || floor.getId().contains("newid"))) {
                    floor.setId(UUID.randomUUID().toString());
                }
            });
        }
    }
}
