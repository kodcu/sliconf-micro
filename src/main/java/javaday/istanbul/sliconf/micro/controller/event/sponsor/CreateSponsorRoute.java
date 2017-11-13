package javaday.istanbul.sliconf.micro.controller.event.sponsor;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Sponsor;
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
import java.util.*;


@Api
@Path("/service/events/sponsor/create/:event-key")
@Produces("application/json")
@Component
public class CreateSponsorRoute implements Route {


    private EventRepositoryService repositoryService;

    @Autowired
    public CreateSponsorRoute(EventRepositoryService eventRepositoryService) {
        this.repositoryService = eventRepositoryService;
    }

    @POST
    @ApiOperation(value = "Creates a sponsor", nickname = "CreateSponsorRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "event-key", paramType = "path"), //
            @ApiImplicitParam(required = true, dataTypeClass = Sponsor.class, name = "sponsor", paramType = "body"), //
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

        Sponsor sponsor = JsonUtil.fromJson(body, Sponsor.class);

        String eventKey = request.params("event-key");

        return addNewSponsor(sponsor, eventKey);
    }

    /**
     * Gelen eventKey ve sponsor ile gerekli evente sponsor eklenir
     * @param sponsor
     * @param eventKey
     * @return
     */
    private ResponseMessage addNewSponsor(Sponsor sponsor, String eventKey) {

        ResponseMessage responseMessage;

        if (Objects.isNull(sponsor) ||
                Objects.isNull(sponsor.getLogo()) || sponsor.getLogo().isEmpty() ||
                Objects.isNull(sponsor.getName()) || sponsor.getName().isEmpty() ||
                Objects.isNull(sponsor.getTag()) || sponsor.getTag().isEmpty()) {
            responseMessage = new ResponseMessage(false,
                    "Sponsor data must be filled, can not be empty!", new Object());
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

        if (Objects.isNull(event.getSponsors())) {
            event.setSponsors(new HashMap<>());
        }

        String tagId = "sp" + sponsor.getTag();

        sponsor.setId(UUID.randomUUID().toString());

        List<Sponsor> sponsors = event.getSponsors().get(tagId);

        if (Objects.nonNull(sponsors)) {
            // add to list
            if (!sponsors.contains(sponsor)) {
                sponsors.add(sponsor);
            }

        } else {
            // put to map
            sponsors = new ArrayList<>();

            sponsors.add(sponsor);
        }

        event.getSponsors().put(tagId, sponsors);

        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        responseMessage = new ResponseMessage(true,
                "Sponsor saved successfully", sponsor);

        return responseMessage;
    }
}
