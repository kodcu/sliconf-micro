package javaday.istanbul.sliconf.micro.controller.event;

import io.swagger.annotations.*;
import javaday.istanbul.sliconf.micro.model.event.Event;
import javaday.istanbul.sliconf.micro.model.response.ResponseMessage;
import javaday.istanbul.sliconf.micro.provider.EventControllerMessageProvider;
import javaday.istanbul.sliconf.micro.service.event.EventRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Objects;


@Api
@Path("/service/events/get/with-key/:key")
@Produces("application/json")
@Component
public class GetEventWithKeyRoute implements Route {

    private EventControllerMessageProvider messageProvider;
    private EventRepositoryService repositoryService;

    @Autowired
    public GetEventWithKeyRoute(EventControllerMessageProvider messageProvider,
                                EventRepositoryService eventRepositoryService) {
        this.messageProvider = messageProvider;
        this.repositoryService = eventRepositoryService;
    }

    @GET
    @ApiOperation(value = "Returns event with given key", nickname = "GetEventWithKeyRoute")
    @ApiImplicitParams({ //
            @ApiImplicitParam(required = true, dataType = "string", name = "token", paramType = "header"), //
            @ApiImplicitParam(required = true, dataType = "string", name = "key", paramType = "path")
    })
    @ApiResponses(value = { //
            @ApiResponse(code = 200, message = "Success", response = ResponseMessage.class), //
            @ApiResponse(code = 400, message = "Invalid input data", response = ResponseMessage.class), //
            @ApiResponse(code = 401, message = "Unauthorized", response = ResponseMessage.class), //
            @ApiResponse(code = 404, message = "User not found", response = ResponseMessage.class) //
    })
    @Override
    public ResponseMessage handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
        ResponseMessage responseMessage;

        String key = request.params("key");

        // event var mı diye kontrol et
        Event event = repositoryService.findEventByKeyEquals(key);

        if (Objects.isNull(event)) {
            responseMessage = new ResponseMessage(false,
                    messageProvider.getMessage("eventCanNotFound"), new Object());
            return responseMessage;
        }

        responseMessage = new ResponseMessage(true,
                messageProvider.getMessage("eventCreatedSuccessfully"), event);

        return responseMessage;
    }
}
