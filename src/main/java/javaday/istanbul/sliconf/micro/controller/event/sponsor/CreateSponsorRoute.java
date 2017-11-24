package javaday.istanbul.sliconf.micro.controller.event.sponsor;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.SponsorsAndSponsorTags;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.event.Sponsor;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import javaday.istanbul.sliconf.micro.specs.SponsorSpecs;
import javaday.istanbul.sliconf.micro.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

        SponsorsAndSponsorTags sponsorsAndSponsorTags = JsonUtil.fromJson(body, SponsorsAndSponsorTags.class);

        String eventKey = request.params("event-key");

        return addNewSponsor(sponsorsAndSponsorTags.getSponsors(), sponsorsAndSponsorTags.getSponsorTags(), eventKey);
    }

    /**
     * Gelen sponsorMap ve sponsorTag gelen eventKey ile gerekli evente yazilir
     *
     * @param sponsorMap
     * @param sponsorTags
     * @param eventKey
     * @return
     */
    private ResponseMessage addNewSponsor(Map<String, List<Sponsor>> sponsorMap, Map<String, String> sponsorTags, String eventKey) {

        ResponseMessage responseMessage;

        responseMessage = SponsorSpecs.isSponsorMapValid(sponsorMap, sponsorTags);

        if (!responseMessage.isStatus()) {
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

        event.setSponsors(sponsorMap);

        ResponseMessage dbResponse = repositoryService.save(event);

        if (!dbResponse.isStatus()) {
            return dbResponse;
        }

        HashMap<String, Object> returnObject = new HashMap<>();

        returnObject.put("sponsors", sponsorMap);
        returnObject.put("sponsorTags", sponsorTags);

        responseMessage = new ResponseMessage(true,
                "Sponsor saved successfully", returnObject);

        return responseMessage;
    }


}
