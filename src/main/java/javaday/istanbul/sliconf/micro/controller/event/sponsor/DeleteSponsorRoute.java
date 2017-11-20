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
import java.util.Objects;


@Api
@Path("/service/events/sponsor/delete/:event-key/:sponsorId/:tag")
@Produces("application/json")
@Component
public class DeleteSponsorRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public DeleteSponsorRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Deletes a sponsor", nickname = "DeleteSponsorRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "sponsorId", paramType = "path"), //
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
        return deleteSponsorFromEvent(request);
    }

    private ResponseMessage deleteSponsorFromEvent(Request request) {
        ResponseMessage responseMessage;

        String sponsorId = request.params("sponsorId");

        if (Objects.isNull(sponsorId) || sponsorId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Sponsor Id can not be empty", new Object());
            return responseMessage;
        }


        String tagId = request.params("tag");

        if (Objects.isNull(tagId) || tagId.isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Tag can not be empty", new Object());
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

        if (Objects.nonNull(event.getSponsors())) {
            event.getSponsors().get(tagId).removeIf(sponsor -> sponsor.getId().equals(sponsorId));
        }

        // eger event yoksa kayit et
        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Sponsor deleted successfully", "");

        return responseMessage;
    }
}
