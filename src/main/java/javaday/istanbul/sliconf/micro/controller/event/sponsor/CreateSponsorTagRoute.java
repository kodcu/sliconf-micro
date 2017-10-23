package javaday.istanbul.sliconf.micro.controller.event.sponsor;

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
import java.util.HashMap;
import java.util.Objects;


@Api
@Path("/service/events/sponsor-tag/create/:event-key/:tag")
@Produces("application/json")
@Component
public class CreateSponsorTagRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public CreateSponsorTagRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates a sponsor tag ang puts in events", nickname = "CreateSponsorTagRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "tag", paramType = "path"), //
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

        String tag = request.params("tag");

        String eventKey = request.params("event-key");

        if (Objects.isNull(tag) || tag.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Sponsor's tag can not empty!", new Object());
            return responseMessage;
        }

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

        if (Objects.isNull(event.getSponsorTags())) {
            event.setSponsorTags(new HashMap<>());
        }

        int tagId = 1;

        while (!event.getSponsorTags().containsKey("st" + tagId)) {
            tagId++;
        }

        event.getSponsorTags().put("st" + tagId, tag);

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Sponsor Tag saved successfully", "st" + tagId);

        return responseMessage;
    }
}
